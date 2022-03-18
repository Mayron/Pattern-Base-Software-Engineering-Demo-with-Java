function callApi<T>(url: string): Promise<T> {
  return fetch(url).then((response) => {
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    return response.json();
  });
}

window.addEventListener("load", () => {
  const orderStatus = document.getElementById("orderStatus") as HTMLDivElement;
  const continueBtn = document.getElementById("continue") as HTMLAnchorElement;
  const fetchStatusUrl = orderStatus.dataset.url as string;

  let interval = setInterval(() => {
    const newStatus = callApi<IOrderStatus>(fetchStatusUrl);
    const status = orderStatus.lastElementChild as HTMLParagraphElement;

    newStatus.then((data) => {
      status.textContent = data.status;

      if (data.status.indexOf("Success") !== -1) {
        clearInterval(interval);
        continueBtn.style.display = "block";
      } else if (data.status.indexOf("accepted")) {
        localStorage.removeItem("cart");
      }
    });
  }, 3000);
});
