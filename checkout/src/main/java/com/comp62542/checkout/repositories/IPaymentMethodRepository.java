package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.PaymentMethod;
import java.util.List;

public interface IPaymentMethodRepository extends IRepository<PaymentMethod> {
    List<PaymentMethod> getCards();
    PaymentMethod getAlternativePaymentMethod(String paymentType);
}
