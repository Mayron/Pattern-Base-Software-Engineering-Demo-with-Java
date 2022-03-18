package com.comp62542.checkout.observers;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.factories.IFactory;
import com.comp62542.checkout.factories.PaymentHandler;
import com.comp62542.checkout.repositories.IOrderRepository;

public class OrderProcessingObserver extends BaseObserver<Ticket> {

    private final IOrderRepository orderRepository;
    private final IFactory<PaymentHandler, String> paymentHandlerFactory;

    public OrderProcessingObserver(
            IOrderRepository orderRepository,
            IFactory<PaymentHandler, String> paymentHandlerFactory) {
        this.orderRepository = orderRepository;
        this.paymentHandlerFactory = paymentHandlerFactory;
    }

    @Override
    public void update(Ticket ticket) {
        if (!ticket.status.equals(AppConstants.ORDER_PROCESSING_STATUS))
            return;

        Order order = this.orderRepository.getByOrderId(ticket.orderId);

        if (order == null)
            return;

        PaymentHandler handler = paymentHandlerFactory.create(ticket.paymentType);
        boolean success = handler.handle(ticket);

        if (success) {
            this.subject.unregister(this);
        }
    }
}
