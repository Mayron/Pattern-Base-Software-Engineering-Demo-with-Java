"use strict";
function callApi(url) {
    return fetch(url).then(function (response) {
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    });
}
window.addEventListener("load", function () {
    var orderStatus = document.getElementById("orderStatus");
    var continueBtn = document.getElementById("continue");
    var fetchStatusUrl = orderStatus.dataset.url;
    var interval = setInterval(function () {
        var newStatus = callApi(fetchStatusUrl);
        var status = orderStatus.lastElementChild;
        newStatus.then(function (data) {
            status.textContent = data.status;
            if (data.status.indexOf("Success") !== -1) {
                clearInterval(interval);
                continueBtn.style.display = "block";
            }
            else if (data.status.indexOf("accepted")) {
                localStorage.removeItem("cart");
            }
        });
    }, 3000);
});
