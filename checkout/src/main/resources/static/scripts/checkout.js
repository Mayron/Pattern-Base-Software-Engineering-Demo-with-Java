"use strict";
window.addEventListener("load", function () {
    var btns = document.querySelectorAll(".remove-from-cart");
    btns.forEach(function (btn) {
        var productId = btn.dataset.id;
        btn.addEventListener("click", function () {
            removeFromCart(productId);
        });
    });
});
