package com.comp62542.checkout.observers;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.repositories.IOrderRepository;
import java.time.LocalDateTime;

public class OrderCompleteObserver extends BaseObserver<Ticket> {

    private final IOrderRepository orderRepository;

    public OrderCompleteObserver(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void update(Ticket ticket) {
        if (!ticket.status.equals(AppConstants.ORDER_COMPLETE_STATUS))
            return;

        Order order = this.orderRepository.getByOrderId(ticket.orderId);

        if (order == null)
            return;

        order.completed = LocalDateTime.now().toString();
        this.orderRepository.add(order);
        this.orderRepository.saveChanges();

        subject.unregister(this);
    }
}
