package com.comp62542.checkout.commands;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.builders.IShoppingCartBuilder;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.repositories.IRepository;
import com.comp62542.checkout.repositories.IShoppingCartRepository;
import java.util.List;

public class RebuildShoppingCartCommand implements ICommand {

    private final IShoppingCartBuilder shoppingCartBuilder;
    private final IShoppingCartRepository shoppingCartRepository;
    private final IRepository<Product> productRepository;
    private final List<ShoppingCartItem> items;
    private final String couponCode;

    public RebuildShoppingCartCommand(
            IShoppingCartBuilder shoppingCartBuilder,
            IShoppingCartRepository shoppingCartRepository,
            IRepository<Product> productRepository,
            List<ShoppingCartItem> items,
            String couponCode) {

        this.shoppingCartBuilder = shoppingCartBuilder;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.items = items;
        this.couponCode = couponCode;
    }

    @Override
    public boolean execute() {
        shoppingCartBuilder.reset();

        for (ShoppingCartItem item : this.items) {
            Product product = productRepository.get(Product.class, item.productId);

            if (product != null) {
                shoppingCartBuilder.addProduct(product, item.quantity);
            }
        }

        if (this.couponCode != null) {
            this.shoppingCartBuilder.applyCoupon(this.couponCode);
        }

        ShoppingCart updatedShoppingCart = shoppingCartBuilder.build();
        ShoppingCart previousShoppingCart = shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        if (previousShoppingCart != null) {
            shoppingCartRepository.delete(previousShoppingCart);
        }

        shoppingCartRepository.add(updatedShoppingCart);
        shoppingCartRepository.saveChanges();

        return true;
    }
}
