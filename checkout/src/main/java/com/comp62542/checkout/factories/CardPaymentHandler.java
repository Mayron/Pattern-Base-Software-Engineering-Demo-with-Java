package com.comp62542.checkout.factories;

import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.repositories.IOrderRepository;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;
import java.util.List;

public class CardPaymentHandler extends PaymentHandler {

    public CardPaymentHandler(IOrderRepository orderRepository, IPaymentMethodRepository paymentMethodRepository) {
        super(orderRepository, paymentMethodRepository);
    }

    @Override
    boolean processPayment(Order order) {
        List<PaymentMethod> cards = paymentMethodRepository.getCards();

        for (PaymentMethod card : cards) {
            String cardId = paymentMethodRepository.getDocumentId(card);

            if (cardId.equals(order.paymentId)) {
                System.out.println(String.format("Paid %s with Card!", order.totalCost));
                return true;
            }
        }

        return false;
    }
}
