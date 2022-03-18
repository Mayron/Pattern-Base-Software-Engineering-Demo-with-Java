package com.comp62542.checkout.domain;

import java.math.BigDecimal;
import java.util.Map;

public class Order {
    public String orderId;
    public String paymentId;
    public String paymentType;
    public BigDecimal totalCost;
    public Map<String, Integer> items;
    public boolean paymentReceived;
    public String completed;
}
