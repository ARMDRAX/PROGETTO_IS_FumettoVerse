<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.OrderItem" %>
<%@ page import="java.util.*" %>
<%@ include file="header.jsp" %>

<title>Il mio carrello</title>
<link rel="stylesheet" href="styles/common.css">
<link rel="stylesheet" href="styles/carrellostyle.css">

<div class="cart-container">
    <h2>Il mio carrello</h2>

    <%
        String err = request.getParameter("error");
        if ("invalidQuantity".equals(err)) {
    %>
        <p style="color:red; font-weight:bold;">
            Quantità non valida: inserisci un numero intero maggiore o uguale a 1 prima di procedere al checkout.
        </p>
    <%
        }
        Boolean empty = (Boolean) request.getAttribute("emptyCart");
        if (empty != null && empty) {
    %>
        <div class="empty-cart-message">
            <img src="images/goku-dragon-ball-guru.jpg" alt="Carrello vuoto" />
            <h3>Il tuo carrello è vuoto</h3>
            <p>Scopri i nostri fumetti e riempi il tuo carrello!</p>
            <a href="index" class="shop-now-btn">Vai allo Shop</a>
        </div>
    <%
        } else {
            List<OrderItem> items = (List<OrderItem>) request.getAttribute("items");
            double total = (double) request.getAttribute("total");

            for (OrderItem item : items) {
                double subtotal = item.getPrice() * item.getQuantity();
    %>
        <div class="cart-item">
            <img src="images/<%= item.getImage() %>" alt="<%= item.getTitle() %>">
            <div class="cart-info">
                <h3><%= item.getTitle() %></h3>
                <div class="price">€ <%= String.format("%.2f", item.getPrice()) %> x <%= item.getQuantity() %></div>
                <div class="price">Subtotale: € <%= String.format("%.2f", subtotal) %></div>

                <div class="cart-actions">
                    <form action="updateCart" method="post">
                        <input type="hidden" name="comicId" value="<%= item.getId() %>">

                        <input type="number"
                               name="quantity"
                               value="<%= item.getQuantity() %>"
                               min="1"
                               step="1"
                               required
                               class="quantity-input">

                        <button type="submit" class="update-btn">Aggiorna</button>
                    </form>

                    <form action="removeFromCart" method="post">
                        <input type="hidden" name="comicId" value="<%= item.getId() %>">
                        <button type="submit" class="remove-btn">Rimuovi</button>
                    </form>
                </div>
            </div>
        </div>
    <%
            }
    %>

    <div class="cart-total">Totale: € <%= String.format("%.2f", total) %></div>

    <div id="cart-error" style="color:red; font-weight:bold; margin-top:10px;"></div>

    <div class="buttons-container">
        <form action="checkout" method="get">
            <button type="submit" id="checkoutBtn" class="checkout-btn">Procedi al Checkout</button>
        </form>
    </div>

    <form action="clearCart" method="post">
        <button type="submit" class="clear-cart-btn">Svuota Carrello</button>
    </form>

    <%
        }
    %>
</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
  const qtyInputs = Array.from(document.querySelectorAll('input.quantity-input'));
  const checkoutBtn = document.getElementById('checkoutBtn');
  const err = document.getElementById('cart-error');

  function refresh() {
    if (!checkoutBtn) return;

    const hasInvalid = qtyInputs.some(i => !i.checkValidity());
    checkoutBtn.disabled = hasInvalid;

    if (hasInvalid) {
      err.textContent = "Correggi le quantità (min 1, solo interi) prima di procedere al checkout.";
    } else {
      err.textContent = "";
    }
  }

  qtyInputs.forEach(i => {
    i.addEventListener('input', refresh);
    i.addEventListener('change', refresh);
  });

  refresh();
});
</script>

<%@ include file="footer.jsp" %>
