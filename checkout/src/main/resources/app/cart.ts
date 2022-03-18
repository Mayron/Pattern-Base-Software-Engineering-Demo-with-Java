const addToCart = (btn: HTMLButtonElement, render: Renderer) => {
  const price = parseFloat(btn.dataset.price as string);
  const name = btn.dataset.name as string;
  const id = btn.dataset.id as string;
  const discount = parseFloat(btn.dataset.discount as string);
  const savings = parseFloat(btn.dataset.savings as string);
  const perUnit = btn.dataset.perunit as string;

  let cartRaw = localStorage.getItem("cart");
  let cart: ICart = cartRaw ? JSON.parse(cartRaw) : { items: [] };
  let existingItem = cart.items.filter((i) => i.name === name);

  if (existingItem.length > 0) {
    existingItem[0].quantity += 1;
  } else {
    cart.items.push({
      id,
      price,
      name,
      quantity: 1,
      discount,
      savings,
      perUnit,
    });
  }

  localStorage.setItem("cart", JSON.stringify(cart));
  render(cart);
};

const getPriceDetails: (item: ICartItem) => IPriceDetails = (item) => {
  let price = item.price * item.quantity;
  let savings = item.savings * item.quantity;

  return { price, savings };
};

const removeFromCart = (productId: string, render?: Renderer) => {
  const cartValue = localStorage.getItem("cart");

  if (!cartValue) {
    return;
  }

  const cart = JSON.parse(cartValue) as ICart;
  cart.items = cart.items.filter((i) => i.id !== productId);
  localStorage.setItem("cart", JSON.stringify(cart));

  render && render(cart);
};
