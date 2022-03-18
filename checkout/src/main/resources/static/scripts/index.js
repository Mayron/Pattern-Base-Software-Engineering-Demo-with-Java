"use strict";
var renderIndexView = function (cart) {
    var cartSummary = document.getElementById("cartSummary");
    var list = cartSummary.getElementsByTagName("ul")[0];
    list.innerHTML = "";
    var cartTotal = 0;
    var cartSavings = 0;
    cart.items.forEach(function (item) {
        var newListItem = document.createElement("li");
        var label = document.createElement("label");
        label.textContent = item.name + " (" + item.quantity + ")";
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = item.id;
        input.value = item.quantity.toString();
        var _a = getPriceDetails(item), price = _a.price, savings = _a.savings;
        cartTotal += price;
        cartSavings += savings;
        var closeBtn = document.createElement("button");
        closeBtn.classList.add("btn-close");
        closeBtn.addEventListener("click", function () {
            return removeFromCart(item.id, renderIndexView);
        });
        closeBtn.title = "Remove from cart";
        newListItem.appendChild(label);
        newListItem.appendChild(input);
        newListItem.appendChild(closeBtn);
        list.appendChild(newListItem);
    });
    var totalElement = document.getElementById("total");
    totalElement.textContent = "\u00A3" + cartTotal.toFixed(2);
    var savingsElement = document.getElementById("savings");
    savingsElement.textContent = "\u00A3" + cartSavings.toFixed(2);
};
window.addEventListener("load", function () {
    var btns = document.querySelectorAll(".add-to-cart");
    btns.forEach(function (btn) {
        btn.addEventListener("click", function () { return addToCart(btn, renderIndexView); });
    });
    var cartValue = localStorage.getItem("cart");
    if (cartValue) {
        var cart = JSON.parse(cartValue);
        renderIndexView(cart);
    }
});
