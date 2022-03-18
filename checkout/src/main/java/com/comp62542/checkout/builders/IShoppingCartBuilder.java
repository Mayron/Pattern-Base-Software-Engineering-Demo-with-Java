package com.comp62542.checkout.builders;

import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;

public interface IShoppingCartBuilder {
    void addProduct(Product product, int quantity);
    void applyCoupon(String couponCode);
    ShoppingCart build();
    void reset();
}
