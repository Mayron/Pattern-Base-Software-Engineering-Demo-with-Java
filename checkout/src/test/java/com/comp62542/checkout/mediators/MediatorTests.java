package com.comp62542.checkout.mediators;

import com.comp62542.checkout.BaseTestCase;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.viewModels.CheckoutViewModel;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediatorTests extends BaseTestCase {

    private IMediator mediator;

    public void setUp() {
        loadDependencies();

        // Create Mediator:
        this.mediator = new Mediator(
                shoppingCartBuilder,
                productRepository,
                shoppingCartRepository,
                paymentMethodRepository,
                helperService,
                ticketService,
                orderRepository,
                ticketRepository,
                ticketStatusUpdater,
                paymentHandlerFactory);
    }

    @DisplayName("Test when calling two mediator methods should handle dependencies, create ShoppingCart " +
            "and fill CheckoutViewModel with items from cart and assign pricing property values.")
    public void testWhenCallingMediatorQueriesAndCommandsShouldHandleDependenciesAndFillModelWithExpectedProperties() {
        // Arrange
        CheckoutViewModel model = new CheckoutViewModel();
        assertNull(model.items);
        assertNull(model.originalPrice);

        List<Product> products = productRepository.getAll(Product.class).subList(0, 4);
        Map<String, String[]> inputs = new HashMap<>();

        for (Product product : products) {
            String productId = productRepository.getDocumentId(product);
            inputs.put(productId, new String[]{"2"});
        }

        // Act
        mediator.handleCheckout(inputs);
        mediator.query(model);

        // Assert
        assertNotNull(model.items);
        assertEquals(4, model.items.size());
        assertEquals("13.12", model.originalPrice);
        assertEquals("12.62", model.finalPrice);
    }
}
