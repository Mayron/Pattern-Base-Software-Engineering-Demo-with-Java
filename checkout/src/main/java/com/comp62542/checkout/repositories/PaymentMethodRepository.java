package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.PaymentMethod;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class PaymentMethodRepository extends BaseRepository<PaymentMethod> implements IPaymentMethodRepository {

    @Override
    public List<PaymentMethod> getCards() {
        return session.query(PaymentMethod.class)
                .waitForNonStaleResults()
                .whereEquals("type", "card")
                .toList();
    }

    @Override
    public PaymentMethod getAlternativePaymentMethod(String paymentType) {
        return session.query(PaymentMethod.class)
                .waitForNonStaleResults()
                .whereEquals("type", paymentType)
                .firstOrDefault();
    }
}
