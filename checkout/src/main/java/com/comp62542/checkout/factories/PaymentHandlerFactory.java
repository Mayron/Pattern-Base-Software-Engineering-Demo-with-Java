package com.comp62542.checkout.factories;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.repositories.IOrderRepository;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentHandlerFactory implements IFactory<PaymentHandler, String> {

    private final IOrderRepository orderRepository;
    private final IPaymentMethodRepository paymentMethodRepository;

    public PaymentHandlerFactory(IOrderRepository orderRepository, IPaymentMethodRepository paymentMethodRepository) {
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public PaymentHandler create(String value) {
        switch (value) {
            case AppConstants.GOOGLE_PAY_PAYMENT_TYPE:
                return new GooglePaymentHandler(this.orderRepository, this.paymentMethodRepository);

            case AppConstants.PAYPAL_PAYMENT_TYPE:
                return new PayPalPaymentHandler(this.orderRepository, this.paymentMethodRepository);

            case AppConstants.CARD_PAYMENT_TYPE:
                return new CardPaymentHandler(this.orderRepository, this.paymentMethodRepository);

            default:
                System.out.println(String.format("Failed to find payment handler for %s", value));
                return null;
        }
    }
}
