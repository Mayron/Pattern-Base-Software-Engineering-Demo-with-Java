package com.comp62542.checkout.viewModels;

import com.comp62542.checkout.models.FiltersModel;

import java.util.List;

public class IndexViewModel {

    public static class Product {
        public String id;
        public String newPrice;
        public String discount;
        public String name;
        public String savings;
        public String perUnit;
        public String originalPrice;
        public String imageName;
        public String type;

        public String getDiscount() {
            return discount;
        }

        public String getName() {
            return name;
        }

        public String getSavings() {
            return savings;
        }

        public String getPerUnit() {
            return perUnit;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public String getImageName() {
            return imageName;
        }

        public String getId() {
            return id;
        }
    }

    public List<Product> products;
    public List<Product> getProducts() {
        return products;
    }

    public FiltersModel filters;
    public FiltersModel getFilters() {
        return filters;
    }

    public String query;
    public String getQuery() {
        return query;
    }
}
