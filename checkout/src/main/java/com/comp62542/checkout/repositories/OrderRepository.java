package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

@Repository
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class OrderRepository extends BaseRepository<Order> implements IOrderRepository {

    @Override
    public Order getByOrderId(String orderId) {
        return session.query(Order.class)
                .waitForNonStaleResults()
                .whereEquals("orderId", orderId)
                .firstOrDefault();
    }
}
