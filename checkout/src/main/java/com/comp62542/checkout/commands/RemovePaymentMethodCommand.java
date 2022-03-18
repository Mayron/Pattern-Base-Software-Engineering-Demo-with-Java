package com.comp62542.checkout.commands;

import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;

public class RemovePaymentMethodCommand implements ICommand {
    private final IPaymentMethodRepository paymentMethodRepository;
    private final String paymentId;

    public RemovePaymentMethodCommand(IPaymentMethodRepository paymentMethodRepository, String paymentId) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentId = paymentId;
    }

    @Override
    public boolean execute() {
        PaymentMethod method = this.paymentMethodRepository.get(PaymentMethod.class, paymentId);

        if (method == null)
            return false;

        this.paymentMethodRepository.delete(method);
        this.paymentMethodRepository.saveChanges();

        return true;
    }
}
