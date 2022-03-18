package com.comp62542.checkout.builders;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.BaseTestCase;
import com.comp62542.checkout.builders.IShoppingCartBuilder;
import com.comp62542.checkout.builders.ShoppingCartBuilder;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;
import com.comp62542.checkout.repositories.IRepository;
import com.comp62542.checkout.repositories.PaymentMethodRepository;
import com.comp62542.checkout.repositories.ProductRepository;
import com.comp62542.checkout.services.HelperService;
import com.comp62542.checkout.services.IHelperService;
import com.comp62542.checkout.singletons.CouponsSingleton;
import com.comp62542.checkout.singletons.DatabaseSingleton;
import org.junit.jupiter.api.DisplayName;
import java.util.*;

public class ShoppingCartBuilderTests extends BaseTestCase {

    private List<Product> products;

    public void setUp() {
        loadDependencies();
        this.products = productRepository.getAll(Product.class);
    }

    @DisplayName("Test when applying an invalid coupon, then cart built should not contain coupon discount")
    public void testShoppingCartBuilderWithInvalidCoupon() {
        // Act
        for (Product product : products) {
            // add products to the builder
            shoppingCartBuilder.addProduct(product, 1);
        }

        shoppingCartBuilder.applyCoupon("Invalid Coupon");

        // Build the Shopping cart
        ShoppingCart cart = shoppingCartBuilder.build();

        // Assert
        assertNull(cart.savingsFromCoupon);
        assertEquals(0, cart.couponDiscountPercentage);
        assertNull(cart.couponApplied);

        assertEquals("15.46", cart.finalPrice.toPlainString());
        assertEquals("2.09", cart.savingsFromDiscount.toPlainString());
        assertEquals("17.55", cart.originalPrice.toPlainString());
        assertEquals(AppConstants.ShoppingCartId, cart.id);

        assertEquals(products.size(), cart.items.size());
    }

    @DisplayName("Test when applying an existing coupon, then cart built should contain coupon discount")
    public void testShoppingCartBuilderWithExistingCoupon() {
        // Arrange
        List<Product> products = productRepository.getAll(Product.class);

        // Act
        for (Product product : products) {
            // add products to the builder
            shoppingCartBuilder.addProduct(product, 1);
        }

        // Apply valid coupon (worth 75% off!)
        shoppingCartBuilder.applyCoupon("XS123");

        // Build the Shopping cart
        ShoppingCart cart = shoppingCartBuilder.build();

        // Assert
        assertEquals("11.60", cart.savingsFromCoupon.toPlainString());
        assertEquals(75, cart.couponDiscountPercentage);
        assertEquals("XS123", cart.couponApplied);

        assertEquals("3.86", cart.finalPrice.toPlainString());
        assertEquals("2.09", cart.savingsFromDiscount.toPlainString());
        assertEquals("17.55", cart.originalPrice.toPlainString());
        assertEquals(AppConstants.ShoppingCartId, cart.id);

        assertEquals(products.size(), cart.items.size());
    }
}
