package com.comp62542.checkout.services;

import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.models.FiltersModel;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;
import com.comp62542.checkout.repositories.IRepository;
import com.comp62542.checkout.viewModels.CheckoutViewModel;
import com.comp62542.checkout.viewModels.IndexViewModel;
import com.comp62542.checkout.viewModels.PaymentViewModel;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class HelperService implements IHelperService {

    private final IRepository<Product> productRepository;
    private final IPaymentMethodRepository paymentMethodRepository;

    public HelperService(IRepository<Product> productRepository, IPaymentMethodRepository paymentMethodRepository) {
        this.productRepository = productRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public BigDecimal calculateDiscount(BigDecimal price, int discount) {
        BigDecimal discountPercentage = new BigDecimal(discount / 100.0)
                .setScale(2, RoundingMode.HALF_UP);

        return price.multiply(discountPercentage)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String formatCurrency(BigDecimal currency) {
        return currency.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal price, BigDecimal discountAmount) {
        return price.subtract(discountAmount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public IndexViewModel.Product map(Product product) {
        BigDecimal discountAmount = calculateDiscount(product.price, product.discount);
        BigDecimal newPrice = applyDiscount(product.price, discountAmount);
        String productId = this.productRepository.getDocumentId(product);

        IndexViewModel.Product productModel = new IndexViewModel.Product();
        productModel.id = productId;
        productModel.newPrice = formatCurrency(newPrice);
        productModel.discount = product.discount > 0 ? product.discount + "% off!" : null;
        productModel.name = product.name;
        productModel.savings = formatCurrency(discountAmount);
        productModel.perUnit = product.perUnit;
        productModel.originalPrice = formatCurrency(product.price);
        productModel.imageName = product.imageName;
        productModel.type = product.type;
        return productModel;
    }

    @Override
    public CheckoutViewModel.Item map(ShoppingCartItem item, Product product) {
        String productId = this.productRepository.getDocumentId(product);

        CheckoutViewModel.Item itemModel = new CheckoutViewModel.Item();
        itemModel.name = product.name;
        itemModel.productId = productId;
        itemModel.image = product.imageName;
        itemModel.perUnit = product.perUnit;
        itemModel.quantity = item.quantity;
        itemModel.pricePerItem = formatCurrency(product.price);
        itemModel.totalPrice = formatCurrency(product.price
                .multiply(new BigDecimal(itemModel.quantity)));

        if (product.discount > 0) {
            itemModel.discount = product.discount + "% off!";
        }

        return itemModel;
    }

    @Override
    public void map(CheckoutViewModel model, ShoppingCart shoppingCart) {
        model.finalPrice = formatCurrency(shoppingCart.finalPrice);
        model.originalPrice = formatCurrency(shoppingCart.originalPrice);
        model.minusDiscount = formatCurrency(shoppingCart.savingsFromDiscount);
        model.couponApplied = shoppingCart.couponApplied;

        if (shoppingCart.couponApplied != null) {
            model.minusCoupon = formatCurrency(shoppingCart.savingsFromCoupon);
            model.couponDiscount = shoppingCart.couponDiscountPercentage + "% off!";
        }
    }

    @Override
    public PaymentViewModel.Card map(PaymentMethod method) {
        PaymentViewModel.Card card = new PaymentViewModel.Card();
        card.id = this.paymentMethodRepository.getDocumentId(method);
        card.nameOnCard = method.name;
        card.cardNumber = method.description;
        card.expiry = method.expiry;
        return card;
    }

    @Override
    public void applyFilters(FiltersModel filters, List<IndexViewModel.Product> products) {
        if (filters.milk || filters.bread || filters.fruit) {
            if (!filters.milk) {
                products.removeIf(p -> p.type.equals("milk"));
            }

            if (!filters.bread) {
                products.removeIf(p -> p.type.equals("bread"));
            }

            if (!filters.fruit) {
                products.removeIf(p -> p.type.equals("fruit"));
            }
        }
    }

    @Override
    public void applySearchQuery(List<IndexViewModel.Product> products, String query) {
        products.removeIf(p -> !p.name.trim().toLowerCase().contains(query.trim().toLowerCase()));
    }
}
