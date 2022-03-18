<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=1280, initial-scale=1.0" />
    <title>Checkout | COMP62542</title>
    <link rel="stylesheet" href="../styles/site.min.css" />
    <script type="text/javascript" src="scripts/cart.js"></script>
    <script type="text/javascript" src="scripts/checkout.js"></script>
  </head>
  <body>
    <div role="banner" class="btn-test">
      <header>
        <h1>
          <a href="<c:url value="/"/>">COMP62542 Grocer Checkout System</a>
        </h1>
      </header>
    </div>
    <main>
      <div id="checkoutPage" class="page">
        <header>
          <h2>Shopping Cart</h2>
        </header>
        <table id="products" class="panel">
          <thead>
            <tr>
              <th>Item</th>
              <th>Quantity</th>
              <th>Price</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <c:choose>
            <c:when test="${model.items.size() gt 0}">
              <c:forEach items="${model.items}" var="item">
                <tr>
                  <td>
                    <div class="product">
                      <img src="images/${item.image}"
                           title="${item.name}"
                           alt="${item.name}"
                      />
                      <div>
                        <h4>${item.name}</h4>
                        <p>&pound;${item.pricePerItem}
                          <c:if test="${not empty item.discount}">
                            <span class="discount">${item.discount}</span>
                          </c:if>
                        </p>
                        <p class="amount-per-unit">${item.perUnit}</p>
                      </div>
                    </div>
                  </td>
                  <td>${item.quantity}</td>
                  <td>&pound;${item.totalPrice}</td>
                  <td>
                    <c:url value="/checkout/remove" var="removeItemUrl">
                      <c:param name="id" value="${item.productId}" />
                    </c:url>
                    <a role="button" data-id="${item.productId}"
                       href="${removeItemUrl}"
                       class="btn-secondary remove-from-cart">Remove</a></td>
                </tr>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <tr><td>Your shopping cart is empty.</td></tr>
            </c:otherwise>
          </c:choose>
          </tbody>
        </table>

        <section id="applyCoupon">
          <c:choose>
            <c:when test="${empty model.couponApplied}">
              <form action="<c:url value="/checkout/addCoupon"/>" method="post">
                <label for="coupon">Have a coupon? Enter it here:</label>
                <input class="form-input-text" id="coupon" name="c" placeholder="Enter coupon code" type="text"/>
                <input class="btn-secondary" type="submit" value="Apply Coupon" />
              </form>
            </c:when>
            <c:otherwise>
              <form action="<c:url value="/checkout/removeCoupon"/>" method="post">
                <p>Coupon applied! <b>${model.couponApplied}</b><span class="discount">${model.couponDiscount}</span></p>
                <input class="btn-secondary" type="submit" value="Remove Coupon" />
              </form>
            </c:otherwise>
          </c:choose>
        </section>

        <c:if test="${model.items.size() gt 0}">
          <section id="breakdown">
            <ul>
              <c:if test="${model.minusDiscount ne '0.00' or not empty model.couponApplied}">
                <li>
                  <p>Original Price: </p>
                  <p>&pound;<span id="original">${model.originalPrice}</span></p>
                </li>
              </c:if>

              <c:if test="${!model.minusDiscount.equals('0.00')}">
                <li>
                  <p>Discounts: </p>
                  <p class="minus">-&pound;<span id="discount">${model.minusDiscount}</span></p>
                </li>
              </c:if>

              <c:if test="${not empty model.couponApplied}">
                <li>
                  <p>Coupon: </p>
                  <p class="minus">-&pound;<span id="couponApplied">${model.minusCoupon}</span></p>
                </li>
              </c:if>
              <li>
                <hr />
              </li>
              <li>
                <p>Total: </p>
                <p>&pound;<span id="final">${model.finalPrice}</span></p>
              </li>
            </ul>
          </section>
        </c:if>

        <section id="actions">
          <a role="button" href="<c:url value="/"/>" class="btn-secondary">Continue Shopping</a>

          <c:if test="${model.items.size() gt 0}">
            <a href="<c:url value="/payment"/>" class="btn-primary">Proceed to Payment</a>
          </c:if>
        </section>
      </div>
    </main>
  </body>
</html>
