package com.comp62542.checkout.domain;

import java.math.BigDecimal;
import java.util.*;

public class ShoppingCart {
    public String id;
    public List<ShoppingCartItem> items;
    public String couponApplied;
    public int couponDiscountPercentage;
    public BigDecimal finalPrice;
    public BigDecimal savingsFromCoupon;
    public BigDecimal savingsFromDiscount;
    public BigDecimal originalPrice;
}
