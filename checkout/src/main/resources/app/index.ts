const renderIndexView: Renderer = (cart) => {
  const cartSummary = document.getElementById("cartSummary") as HTMLElement;
  const list = cartSummary.getElementsByTagName("ul")[0] as HTMLUListElement;
  list.innerHTML = "";

  let cartTotal = 0;
  let cartSavings = 0;

  cart.items.forEach((item) => {
    const newListItem = document.createElement("li");
    const label = document.createElement("label");

    label.textContent = `${item.name} (${item.quantity})`;

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = item.id;
    input.value = item.quantity.toString();

    let { price, savings } = getPriceDetails(item);
    cartTotal += price;
    cartSavings += savings;

    const closeBtn = document.createElement("button");
    closeBtn.classList.add("btn-close");
    closeBtn.addEventListener("click", () =>
      removeFromCart(item.id, renderIndexView)
    );
    closeBtn.title = "Remove from cart";

    newListItem.appendChild(label);
    newListItem.appendChild(input);
    newListItem.appendChild(closeBtn);

    list.appendChild(newListItem);
  });

  const totalElement = document.getElementById("total") as HTMLSpanElement;
  totalElement.textContent = `£${cartTotal.toFixed(2)}`;

  const savingsElement = document.getElementById("savings") as HTMLSpanElement;
  savingsElement.textContent = `£${cartSavings.toFixed(2)}`;
};

window.addEventListener("load", function () {
  const btns = document.querySelectorAll<HTMLButtonElement>(".add-to-cart");

  btns.forEach((btn) => {
    btn.addEventListener("click", () => addToCart(btn, renderIndexView));
  });

  const cartValue = localStorage.getItem("cart");

  if (cartValue) {
    const cart = JSON.parse(cartValue) as ICart;
    renderIndexView(cart);
  }
});
