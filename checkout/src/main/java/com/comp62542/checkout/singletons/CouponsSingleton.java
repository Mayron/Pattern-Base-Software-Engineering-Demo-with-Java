package com.comp62542.checkout.singletons;

import java.util.HashMap;
import java.util.Map;

public final class CouponsSingleton {
    private final Map<String, Integer> coupons;
    private static CouponsSingleton instance;

    public static CouponsSingleton getInstance() {
        if (instance == null) {
            // should go to database
            Map<String, Integer> coupons = new HashMap<>();
            coupons.put("CA32L", 5);
            coupons.put("NPA31", 15);
            coupons.put("HL65D", 20);
            coupons.put("XS123", 75);

            instance = new CouponsSingleton(coupons);
        }

        return instance;
    }

    private CouponsSingleton(Map<String, Integer> coupons) {
        this.coupons = coupons;
    }

    public int getCouponValue(String couponCode) {
        return this.coupons.get(couponCode);
    }

    public boolean couponExists(String couponCode) {
        return this.coupons.containsKey(couponCode);
    }
}
