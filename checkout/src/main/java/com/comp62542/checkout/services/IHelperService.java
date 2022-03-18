package com.comp62542.checkout.services;

import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.domain.Product;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.ShoppingCartItem;
import com.comp62542.checkout.models.FiltersModel;
import com.comp62542.checkout.viewModels.CheckoutViewModel;
import com.comp62542.checkout.viewModels.IndexViewModel;
import com.comp62542.checkout.viewModels.PaymentViewModel;
import java.math.BigDecimal;
import java.util.List;

public interface IHelperService {
    BigDecimal calculateDiscount(BigDecimal price, int discount);
    String formatCurrency(BigDecimal price);
    BigDecimal applyDiscount(BigDecimal price, BigDecimal discountAmount);
    IndexViewModel.Product map(Product product);
    CheckoutViewModel.Item map(ShoppingCartItem item, Product product);
    void map(CheckoutViewModel model, ShoppingCart shoppingCart);
    PaymentViewModel.Card map(PaymentMethod method);
    void applyFilters(FiltersModel filters, List<IndexViewModel.Product> products);
    void applySearchQuery(List<IndexViewModel.Product> products, String query);
}
