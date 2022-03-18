package com.comp62542.checkout.builders;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.repositories.IRepository;
import com.comp62542.checkout.services.IHelperService;
import com.comp62542.checkout.singletons.CouponsSingleton;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartBuilder implements IShoppingCartBuilder {

    private final CouponsSingleton couponsSingleton;
    private final Map<Product, Integer> items;
    private int couponDiscount;
    private final IHelperService helperService;
    private final IRepository<Product> productRepository;
    private String couponApplied;

    public ShoppingCartBuilder(
            IHelperService helperService,
            IRepository<Product> productRepository) {

        this.helperService = helperService;
        this.productRepository = productRepository;
        this.items = new HashMap<>();
        this.couponsSingleton = CouponsSingleton.getInstance();
    }

    public void reset() {
        this.items.clear();
        this.couponApplied = null;
        this.couponDiscount = 0;
    }

    public void addProduct(Product product, int quantity) {
        if (this.items.containsKey(product)) {
            int originalQuantity = this.items.get(product);
            this.items.replace(product, originalQuantity + quantity);
        }
        else {
            this.items.put(product, quantity);
        }
    }

    public void applyCoupon(String couponCode) {
        boolean couponExists = this.couponsSingleton.couponExists(couponCode);

        if (!couponExists) {
            return;
        }

        this.couponDiscount = this.couponsSingleton.getCouponValue(couponCode);
        this.couponApplied = couponCode;
    }

    public ShoppingCart build() {
        List<ShoppingCartItem> items = new ArrayList<>();
        BigDecimal totalOriginalPrice = new BigDecimal("0");
        BigDecimal savingsFromDiscount = new BigDecimal("0");

        // Add shopping cart items
        for (Map.Entry<Product, Integer> entry : this.items.entrySet()) {
            Product product = entry.getKey();
            String productId = this.productRepository.getDocumentId(product);
            int quantity = entry.getValue();

            BigDecimal originalPrice = product.price.multiply(new BigDecimal(quantity))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal discountAmount = this.helperService
                    .calculateDiscount(originalPrice, product.discount);

            ShoppingCartItem item = new ShoppingCartItem();
            item.productId = productId;
            item.quantity = quantity;
            items.add(item);

            totalOriginalPrice = totalOriginalPrice.add(originalPrice).setScale(2, RoundingMode.HALF_UP);
            savingsFromDiscount = savingsFromDiscount.add(discountAmount).setScale(2, RoundingMode.HALF_UP);
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.id = AppConstants.ShoppingCartId;
        shoppingCart.savingsFromDiscount = savingsFromDiscount;
        shoppingCart.items = items;
        shoppingCart.originalPrice = totalOriginalPrice;

        shoppingCart.finalPrice = totalOriginalPrice
                .subtract(savingsFromDiscount)
                .setScale(2, RoundingMode.HALF_UP);

        // apply coupon if is exists
        if (this.couponDiscount > 0) {
            BigDecimal discountAmount = this.helperService
                    .calculateDiscount(shoppingCart.finalPrice, this.couponDiscount);

            shoppingCart.savingsFromCoupon = discountAmount;
            shoppingCart.couponApplied = this.couponApplied;
            shoppingCart.couponDiscountPercentage = this.couponDiscount;

            shoppingCart.finalPrice = shoppingCart.finalPrice
                    .subtract(discountAmount)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return shoppingCart;
    }
}
