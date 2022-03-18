window.addEventListener("load", function () {
  const btns = document.querySelectorAll<HTMLButtonElement>(
    ".remove-from-cart"
  );

  btns.forEach((btn) => {
    const productId = btn.dataset.id as string;
    btn.addEventListener("click", () => {
      removeFromCart(productId);
    });
  });
});
