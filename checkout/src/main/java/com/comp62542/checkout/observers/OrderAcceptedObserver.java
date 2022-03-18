package com.comp62542.checkout.observers;

import com.comp62542.checkout.AppConstants;
import com.comp62542.checkout.domain.ShoppingCart;
import com.comp62542.checkout.domain.Ticket;
import com.comp62542.checkout.repositories.IShoppingCartRepository;

public class OrderAcceptedObserver extends BaseObserver<Ticket> {

    private final IShoppingCartRepository shoppingCartRepository;

    public OrderAcceptedObserver(IShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void update(Ticket ticket) {
        if (!ticket.status.equals(AppConstants.ORDER_ACCEPTED_STATUS))
            return;

        ShoppingCart shoppingCart = this.shoppingCartRepository.getByShoppingCartId(AppConstants.ShoppingCartId);

        if (shoppingCart == null)
            return;

        this.shoppingCartRepository.delete(shoppingCart);
        this.shoppingCartRepository.saveChanges();
        this.subject.unregister(this);
    }
}
