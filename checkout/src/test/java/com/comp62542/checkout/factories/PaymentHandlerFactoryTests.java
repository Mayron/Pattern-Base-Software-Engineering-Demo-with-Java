package com.comp62542.checkout.factories;

import com.comp62542.checkout.BaseTestCase;
import com.comp62542.checkout.domain.Product;
import junit.framework.TestCase;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

public class PaymentHandlerFactoryTests extends BaseTestCase {

    public void setUp() {
        loadDependencies();
    }

    @DisplayName("Test when calling create on factory with card payment type, " +
            "should return instance of CardPaymentHandler")
    public void testPaymentHandlerFactoryWhenPaymentTypeIsCard() {
        // Arrange
        IFactory<PaymentHandler, String> factory = new PaymentHandlerFactory(orderRepository, paymentMethodRepository);
        String paymentType = "card";

        // Act
        PaymentHandler handler = factory.create(paymentType);

        // Assert
        assertEquals("Factory should return instance of CardPaymentHandler",
                CardPaymentHandler.class, handler.getClass());
    }

    @DisplayName("Test when calling create on factory with google pay payment type, " +
            "should return instance of GooglePayPaymentHandler")
    public void testPaymentHandlerFactoryWhenPaymentTypeIsGooglePay() {
        // Arrange
        IFactory<PaymentHandler, String> factory = new PaymentHandlerFactory(orderRepository, paymentMethodRepository);
        String paymentType = "gpay";

        // Act
        PaymentHandler handler = factory.create(paymentType);

        // Assert
        assertEquals("Factory should return instance of GooglePaymentHandler",
                GooglePaymentHandler.class, handler.getClass());
    }

    @DisplayName("Test when calling create on factory with PayPal payment type, " +
            "should return instance of PayPalPaymentHandler")
    public void testPaymentHandlerFactoryWhenPaymentTypeIsPayPal() {
        // Arrange
        IFactory<PaymentHandler, String> factory = new PaymentHandlerFactory(orderRepository, paymentMethodRepository);
        String paymentType = "paypal";

        // Act
        PaymentHandler handler = factory.create(paymentType);

        // Assert
        assertEquals("Factory should return instance of PayPalPaymentHandler",
                PayPalPaymentHandler.class, handler.getClass());
    }
}
