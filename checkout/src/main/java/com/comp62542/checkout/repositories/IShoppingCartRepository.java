package com.comp62542.checkout.repositories;

import com.comp62542.checkout.domain.ShoppingCart;

public interface IShoppingCartRepository extends IRepository<ShoppingCart> {
    ShoppingCart getByShoppingCartId(String id);
}
