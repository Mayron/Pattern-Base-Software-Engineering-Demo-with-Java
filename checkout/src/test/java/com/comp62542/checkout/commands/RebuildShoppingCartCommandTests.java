package com.comp62542.checkout.commands;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.BaseTestCase;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;

public class RebuildShoppingCartCommandTests extends BaseTestCase {

    private List<Product> products;

    public void setUp() {
        loadDependencies();
        this.products = productRepository.getAll(Product.class);
    }

    @DisplayName("Test when executing RebuildShoppingCartCommand and no shopping cart exists then should create it")
    public void testRebuildShoppingCartCommandWhenNoShoppingCartExists() {
        // Arrange
        List<ShoppingCartItem> items = new ArrayList<>();

        for (Product product : products) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.quantity = 1;
            item.productId = productRepository.getDocumentId(product);
            items.add(item);
        }

        ICommand rebuildShoppingCartCommand = new RebuildShoppingCartCommand(
                shoppingCartBuilder, shoppingCartRepository, productRepository, items, "XS123");

        ShoppingCart cartBeforeExecuting = shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);
        assertNull("Cart should not exists until command executed", cartBeforeExecuting);

        // Act
        rebuildShoppingCartCommand.execute();

        // Assert
        ShoppingCart cart = shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);
        assertNotNull(cart);
        assertEquals(10, cart.items.size());
        assertEquals(AppConstants.ShoppingCartId, cart.id);
        assertEquals("XS123", cart.couponApplied);
    }

    @DisplayName("Test when executing RebuildShoppingCartCommand and shopping cart exists then should update it")
    public void testRebuildShoppingCartCommandWheShoppingCartPreviouslyExists() {
        // Arrange
        List<ShoppingCartItem> items = new ArrayList<>();

        for (Product product : products) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.quantity = 2;
            item.productId = productRepository.getDocumentId(product);
            items.add(item);
        }

        ICommand firstRebuildCommand = new RebuildShoppingCartCommand(
                shoppingCartBuilder, shoppingCartRepository, productRepository, items, null);

        // Act
        firstRebuildCommand.execute();
        ShoppingCart firstCart = shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        // Assert
        assertEquals(10, firstCart.items.size());
        assertEquals(AppConstants.ShoppingCartId, firstCart.id);
        assertNull(firstCart.couponApplied);
        assertEquals("30.92", firstCart.finalPrice.toPlainString());

        // Arrange
        ICommand secondRebuildCommand = new RebuildShoppingCartCommand(
                shoppingCartBuilder, shoppingCartRepository, productRepository,
                items.subList(0, 5), "XS123");

        // Act
        secondRebuildCommand.execute();
        ShoppingCart secondCart = shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        // Assert
        assertEquals(5, secondCart.items.size());
        assertEquals(AppConstants.ShoppingCartId, secondCart.id);
        assertEquals("XS123", secondCart.couponApplied);
        assertEquals("3.63", secondCart.finalPrice.toPlainString());
    }
}
