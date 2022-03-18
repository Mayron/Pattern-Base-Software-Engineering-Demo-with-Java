package com.comp62542.checkout.factories;

import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.repositories.IOrderRepository;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;

public class PayPalPaymentHandler extends PaymentHandler {

    public PayPalPaymentHandler(IOrderRepository orderRepository, IPaymentMethodRepository paymentMethodRepository) {
        super(orderRepository, paymentMethodRepository);
    }

    @Override
    boolean processPayment(Order order) {
        PaymentMethod method = paymentMethodRepository.getAlternativePaymentMethod(order.paymentType);
        String methodId = paymentMethodRepository.getDocumentId(method);

        if (methodId.equals(order.paymentId)) {
            System.out.println(String.format("Paid %s with PayPal!", order.totalCost));
            return true;
        }

        return true;
    }
}
