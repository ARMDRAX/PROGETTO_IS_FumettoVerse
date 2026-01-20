<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.OrderItem" %>
<%@ include file="header.jsp" %>

<meta charset="UTF-8">
<title>I tuoi ordini</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/checkoutstyle.css">

<div class="container">
    <h2>I tuoi ordini</h2>

    <%
        List<Order> orders = (List<Order>) request.getAttribute("orders");

        if (orders == null || orders.isEmpty()) {
    %>
        <div class="no-orders-message">
            <img src="images/ordini.jpg" alt="Nessun ordine" />
            <h3>Non hai ancora effettuato ordini</h3>
            <p>Esplora il nostro catalogo e trova il tuo prossimo fumetto!</p>
            <a href="index" class="shop-now-btn">Vai allo Shop</a>
        </div>
    <%
        } else {
            for (Order order : orders) {
    %>
        <div class="order-block">
            <h3>Ordine #<%= order.getOrderNumber() %></h3>
            <p>Data: <%= order.getOrderDate() != null ? order.getOrderDate().toString() : "Data non disponibile" %></p>
            <p>Metodo di pagamento: <%= order.getPaymentMethod() %></p>
            <p>Stato ordine: <strong><%= order.getStatus() %></strong></p>
            <p><strong>Totale ordine: € <%= String.format("%.2f", order.getTotalPrice()) %></strong></p>

            <h4>Fumetti ordinati:</h4>
            <ul>
                <%
                    for (OrderItem item : order.getItems()) {
                        double subtotal = item.getPrice() * item.getQuantity();
                %>
                    <li>
                        <strong><%= item.getTitle() %></strong>
                        – Quantità: <%= item.getQuantity() %>
                        – Prezzo: € <%= String.format("%.2f", item.getPrice()) %>
                        – Subtotale: € <%= String.format("%.2f", subtotal) %>
                    </li>
                <%
                    }
                %>
            </ul>
            <hr>
        </div>
    <%
            }
        }
    %>
</div>

<%@ include file="footer.jsp" %>

