"use strict";
var addToCart = function (btn, render) {
    var price = parseFloat(btn.dataset.price);
    var name = btn.dataset.name;
    var id = btn.dataset.id;
    var discount = parseFloat(btn.dataset.discount);
    var savings = parseFloat(btn.dataset.savings);
    var perUnit = btn.dataset.perunit;
    var cartRaw = localStorage.getItem("cart");
    var cart = cartRaw ? JSON.parse(cartRaw) : { items: [] };
    var existingItem = cart.items.filter(function (i) { return i.name === name; });
    if (existingItem.length > 0) {
        existingItem[0].quantity += 1;
    }
    else {
        cart.items.push({
            id: id,
            price: price,
            name: name,
            quantity: 1,
            discount: discount,
            savings: savings,
            perUnit: perUnit,
        });
    }
    localStorage.setItem("cart", JSON.stringify(cart));
    render(cart);
};
var getPriceDetails = function (item) {
    var price = item.price * item.quantity;
    var savings = item.savings * item.quantity;
    return { price: price, savings: savings };
};
var removeFromCart = function (productId, render) {
    var cartValue = localStorage.getItem("cart");
    if (!cartValue) {
        return;
    }
    var cart = JSON.parse(cartValue);
    cart.items = cart.items.filter(function (i) { return i.id !== productId; });
    localStorage.setItem("cart", JSON.stringify(cart));
    render && render(cart);
};
