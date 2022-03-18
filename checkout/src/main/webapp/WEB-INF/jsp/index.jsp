<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=1280, initial-scale=1.0" />
    <title>Home | COMP62542</title>

    <link rel="stylesheet" href="styles/site.min.css" />
    <script type="text/javascript" src="scripts/cart.js"></script>
    <script type="text/javascript" src="scripts/index.js"></script>
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
      <aside id="filters">
        <form action="<c:url value="/filter"/>" method="post">
          <section class="list">
            <h3>Filter by Category</h3>
            <ul>
              <li>
                <input id="cat1" name="milk" type="checkbox" ${model.filters.milk ? "checked" : ""} />
                <label for="cat1">Milk</label>
              </li>
              <li>
                <input id="cat2" name="bread" type="checkbox" ${model.filters.bread ? "checked" : ""} />
                <label for="cat2">Bread</label>
              </li>
              <li>
                <input id="cat3" name="fruit" type="checkbox" ${model.filters.fruit ? "checked" : ""} />
                <label for="cat3">Fruit</label>
              </li>
            </ul>
          </section>
          <input type="submit" class="btn-secondary" value="Apply Filters" />
        </form>
      </aside>
      <div id="indexPage" class="page">
        <header>
          <div id="overview">
            <c:if test="${model.query != null}">
              <a class="btn-secondary" href="<c:url value="/"/>">Back</a>
            </c:if>
            <c:set value="${model.products.size()}" var="total" />
            <h2>${total} ${total > 1 ? "Products" : "Product"}</h2>
          </div>
          <div id="search">
            <form action="<c:url value="/search"/>" method="post">
              <input type="search" name="query" placeholder="Find a product" />
              <button type="submit">
                <img src="images/search-icon.png" class="icon" alt="Search" />
              </button>
            </form>
          </div>
        </header>
        <c:if test="${model.query != null}">
          <p>Search results for "${model.query}"</p>
        </c:if>
        <div class="panel">
          <ul id="products">
            <c:forEach items="${model.products}" var="product">
              <li>
                <img src="images/${product.imageName}" title="${product.name}" alt="${product.name}" />
                <h4>${product.name}</h4>
                <p>
                <span>&pound;${product.originalPrice}
                  <c:if test="${product.discount != null}">
                    <span class="discount">${product.discount}</span>
                  </c:if>
                </span>
                <span>${product.perUnit}</span>
                </p>
                <button
                  type="button"
                  class="btn-primary add-to-cart"
                  data-price="${product.originalPrice}"
                  data-discount="${product.discount}"
                  data-id="${product.id}"
                  data-name="${product.name}"
                  data-savings="${product.savings}"
                  data-perUnit="${product.perUnit}"
                >
                  Add to Cart
                </button>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>
      <aside id="cartSummary">
        <form action="<c:url value="/checkout"/>" method="post">
          <section class="list">
            <h3>Shopping Cart</h3>
            <ul></ul>
          </section>
          <div>
            <p>Total: <span id="total">&pound;0.00</span></p>
            <p>Savings: <span id="savings">&pound;0.00</span></p>
          </div>
          <input type="submit" class="btn-primary" value="Checkout" />
        </form>
      </aside>
    </main>
  </body>
</html>
