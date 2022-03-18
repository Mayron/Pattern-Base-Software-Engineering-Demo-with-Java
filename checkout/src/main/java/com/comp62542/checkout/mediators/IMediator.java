package com.comp62542.checkout.mediators;

import com.comp62542.checkout.models.OrderStatusModel;
import com.comp62542.checkout.viewModels.CheckoutViewModel;
import com.comp62542.checkout.viewModels.IndexViewModel;
import com.comp62542.checkout.viewModels.PaymentViewModel;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface IMediator {
    void query(IndexViewModel model, HttpSession session);
    void query(CheckoutViewModel model);
    void query(PaymentViewModel model, boolean addCard);
    void query(OrderStatusModel model, String ticketId);
    void handleCheckout(Map<String, String[]> parameterMap);
    void handleCheckoutRemove(String productId);
    void handleCheckoutAddCoupon(String couponCode);
    void handleCheckoutRemoveCoupon();
    void handlePaymentAddCard(String name, String number, String expiry);
    void handlePaymentRemoveCard(String cardId);
    String handleNewOrder(String paymentType, String paymentId);
    void handleIndexFilter(HttpSession session, String milk, String bread, String fruit);
    void handleIndexSearch(HttpSession session, String query);
}
