package com.comp62542.checkout.observers;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.domain.Order;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.repositories.IRepository;
import com.comp62542.checkout.repositories.IShoppingCartRepository;
import java.util.HashMap;

public class OrderCreatedObserver extends BaseObserver<Ticket> {

    private final IRepository<Order> orderRepository;
    private final IShoppingCartRepository shoppingCartRepository;

    public OrderCreatedObserver(IRepository<Order> orderRepository, IShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void update(Ticket ticket) {
        if (!ticket.status.equals(AppConstants.ORDER_CREATED_STATUS))
            return;

        Order order = this.orderRepository.get(Order.class, ticket.orderId);
        ShoppingCart shoppingCart = this.shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        if (order != null || shoppingCart == null)
            return;

        order = new Order();
        order.items = new HashMap<>();

        for (ShoppingCartItem item : shoppingCart.items) {
            order.items.put(item.productId, item.quantity);
        }

        order.orderId = ticket.orderId;
        order.paymentId = ticket.paymentId;
        order.paymentType = ticket.paymentType;
        order.totalCost = shoppingCart.finalPrice;

        this.orderRepository.add(order);
        this.orderRepository.saveChanges();
        this.subject.unregister(this);
    }
}
