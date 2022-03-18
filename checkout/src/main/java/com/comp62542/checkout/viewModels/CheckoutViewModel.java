package com.comp62542.checkout.viewModels;

import java.util.List;

public class CheckoutViewModel {
    public List<Item> items;
    public String originalPrice;
    public String couponApplied;
    public String minusDiscount;
    public String minusCoupon;
    public String finalPrice;
    public String couponDiscount;

    public List<Item> getItems() {
        return items;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getCouponApplied() {
        return couponApplied;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public String getMinusDiscount() {
        return minusDiscount;
    }

    public String getMinusCoupon() {
        return minusCoupon;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public static class Item {
        public String name;
        public String productId;
        public String image;
        public String discount;
        public int quantity;
        public String totalPrice;
        public String pricePerItem;
        public String perUnit;

        public String getName() {
            return name;
        }

        public String getProductId() {
            return productId;
        }

        public String getImage() {
            return image;
        }

        public String getDiscount() {
            return discount;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getPricePerItem() {
            return pricePerItem;
        }

        public String getPerUnit() {
            return perUnit;
        }

        public String getTotalPrice() {
            return totalPrice;
        }
    }
}
