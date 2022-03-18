<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=1280, initial-scale=1.0" />
    <title>Payment | COMP62542</title>

    <link rel="stylesheet" href="styles/site.min.css" />
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
      <div id="paymentPage" class="page">
        <header>
          <h2>Choose a Payment Method</h2>
        </header>
        <section class="panel">
          <header>
            <h3>Your credit or debit cards</h3>
          </header>
          <table>
            <thead>
              <tr>
                <th>Card number</th>
                <th>Name on card</th>
                <th>Expiry date</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty model.cards}">
                <tr>
                  <td colspan="4">You have not added any cards yet.</td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach items="${model.cards}" var="card">
                  <tr>
                    <td>${card.cardNumber}</td>
                    <td>${card.nameOnCard}</td>
                    <td>${card.expiry}</td>
                    <td class="card-actions">
                      <form action="<c:url value="/payment/removeCard"/>" method="post">
                        <input type="hidden" name="id" value="${card.id}">
                        <input type="submit" class="btn-secondary" value="Delete" />
                      </form>

                      <form action="<c:url value="/order"/>" method="post">
                        <input type="hidden" name="type" value="card" />
                        <input type="hidden" name="id" value="${card.id}" />
                        <input type="submit" class="btn-primary" value="Select" />
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            <c:if test="${model.addCard}">
              <tr>
                <td id="cardFields" colspan="4">
                  <form action="<c:url value="/payment/addCard"/>" method="post">
                    <div>
                      <label for="enterCardName">Name on card</label>
                      <input id="enterCardName" class="form-input-text" type="text"
                             required name="name" placeholder="Enter name on card" />
                    </div>

                    <div>
                      <label for="enterCardNum">Card number</label>
                      <input id="enterCardNum" type="text" class="form-input-text"
                             required name="num" placeholder="Enter card number" />
                    </div>

                    <div>
                      <label for="enterCardExpiration">Expiration date</label>
                      <input id="enterCardExpiration" class="form-input-text" required
                             type="date" name="exp" />
                    </div>

                    <input type="submit" class="btn-primary" value="Add your card">
                  </form>

                  <div><a role="button" class="btn-secondary" href="<c:url value="/payment"/>">Cancel</a></div>
                </td>
              </tr>
            </c:if>
            </tbody>
          </table>
          <c:if test="${!model.addCard}">
            <div id="addCard">
              <c:url value="/payment" var="addCardUrl">
                <c:param name="add" value="true" />
              </c:url>
              <a href="${addCardUrl}" class="btn-secondary">Add New Card</a>
            </div>
          </c:if>
          <hr />
          <h3>Alternative Payment Methods</h3>
          <ul id="alternativePayment">
            <li>
              <form action="<c:url value="/order"/>" method="post">
                <input type="hidden" name="type" value="paypal" />
                <input type="hidden" name="id" value="${model.paypalId}" />
                <input type="image" src="images/paypal-logo.png" title="PayPal" alt="PayPal" />
              </form>
            </li>
            <li>
              <form action="<c:url value="/order"/>" method="post">
                <input type="hidden" name="type" value="gpay" />
                <input type="hidden" name="id" value="${model.googlePayId}" />
                <input type="image" src="images/google-pay-logo.png" title="Google Pay" alt="Google Pay" />
              </form>
            </li>
          </ul>
        </section>
        <a class="btn-secondary" href="<c:url value="/checkout"/>">Back to Cart</a>
      </div>
    </main>
  </body>
</html>
