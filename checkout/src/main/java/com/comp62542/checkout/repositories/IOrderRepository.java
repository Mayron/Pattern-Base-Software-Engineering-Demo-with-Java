package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.Order;

public interface IOrderRepository extends IRepository<Order> {
    Order getByOrderId(String orderId);
}
