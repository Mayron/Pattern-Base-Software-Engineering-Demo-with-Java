package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.ShoppingCart;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

@Repository
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartRepository extends BaseRepository<ShoppingCart> implements IShoppingCartRepository {

    @Override
    public ShoppingCart getByShoppingCartId(String id) {
        return session.query(ShoppingCart.class)
                .waitForNonStaleResults()
                .whereEquals("id", id)
                .firstOrDefault();
    }
}
