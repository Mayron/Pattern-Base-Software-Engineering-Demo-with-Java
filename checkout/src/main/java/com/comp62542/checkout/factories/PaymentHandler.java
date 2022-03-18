package com.comp62542.checkout.factories;

import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.repositories.IOrderRepository;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;

public abstract class PaymentHandler {
    protected final IOrderRepository orderRepository;
    protected final IPaymentMethodRepository paymentMethodRepository;

    public PaymentHandler(IOrderRepository orderRepository, IPaymentMethodRepository paymentMethodRepository) {
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public boolean handle(Ticket ticket) {
        Order order = orderRepository.getByOrderId(ticket.orderId);

        boolean success = processPayment(order);

        if (success) {
            order.paymentReceived = true;
            this.orderRepository.add(order);
            this.orderRepository.saveChanges();
        }

        return success;
    }

    // Factory Template Method
    abstract boolean processPayment(Order order);
}
