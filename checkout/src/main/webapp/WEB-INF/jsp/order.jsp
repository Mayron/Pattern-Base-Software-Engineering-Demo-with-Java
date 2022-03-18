<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=1280, initial-scale=1.0" />
  <title>Order | COMP62542</title>

  <link rel="stylesheet" href="styles/site.min.css" />
  <script type="text/javascript" src="./scripts/order.js"></script>
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
  <div id="orderPage" class="page">
    <header>
      <h2>Please wait while we process your order...</h2>
    </header>

    <c:url value="/order/status" var="fetchStatusUrl">
      <c:param name="t" value="${model.ticketId}" />
    </c:url>

    <section id="orderStatus" data-url="${fetchStatusUrl}">
      <h3>Current Status:</h3>
      <p>Getting Ready</p>
    </section>

    <a id="continue" style="display: none;" class="btn-primary" href="<c:url value="/"/>">
      Continue Shopping
    </a>
  </div>
</main>
</body>
</html>
