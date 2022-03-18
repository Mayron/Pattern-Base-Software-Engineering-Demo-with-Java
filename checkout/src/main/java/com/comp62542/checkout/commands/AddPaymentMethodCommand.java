package com.comp62542.checkout.commands;

import com.comp62542.checkout.domain.PaymentMethod;
import com.comp62542.checkout.repositories.IPaymentMethodRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPaymentMethodCommand implements ICommand {
    private final IPaymentMethodRepository paymentMethodRepository;
    private final String name;
    private final String number;
    private final String expiry;

    public AddPaymentMethodCommand(
            IPaymentMethodRepository paymentMethodRepository, String name, String number, String expiry) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.name = name;
        this.number = number;
        this.expiry = expiry;
    }

    @Override
    public boolean execute() {
        PaymentMethod method = new PaymentMethod();

        try {
            DateFormat formFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateExpiry = formFormat.parse(expiry);

            DateFormat cardFormat = new SimpleDateFormat("MM/yy");
            method.expiry = cardFormat.format(dateExpiry);

        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            return false;
        }

        method.type = "card";
        method.description = number;
        method.name = name;

        this.paymentMethodRepository.add(method);
        this.paymentMethodRepository.saveChanges();
        return true;
    }
}
