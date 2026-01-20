<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Order" %>
<%@ page import="model.OrderItem" %>

<%
    // Nel progetto la sessione admin sembra basarsi su isAdmin (Boolean), non su "role"
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String fromDate = (String) request.getAttribute("fromDate");
    String toDate   = (String) request.getAttribute("toDate");

    // Cambiato: filtro per email (non "customer")
    String email = (String) request.getAttribute("email");

    List<Order> orders = (List<Order>) request.getAttribute("orders");
    Boolean error = (Boolean) request.getAttribute("error");

    String msg    = (String) request.getAttribute("message");
    String errMsg = (String) request.getAttribute("errorMessage");
%>

<html>
<head>
    <title>Visualizza Ordini</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<header>
    <div class="logo">FumettoVerse - Gestore Ordini</div>
</header>

<div class="container">

    <h2>Storico Ordini</h2>

    <div class="button-container" style="margin-bottom:20px;">
        <a href="<%= request.getContextPath() %>/admin/admin.jsp" class="action-button">
            Torna al pannello gestore
        </a>
    </div>

    <%
        if (msg != null) {
    %>
        <p style="color:green; text-align:center; font-weight:bold;"><%= msg %></p>
    <%
        } else if (errMsg != null) {
    %>
        <p style="color:red; text-align:center; font-weight:bold;"><%= errMsg %></p>
    <%
        }
    %>

    <div class="form-container">
        <form method="get" action="<%= request.getContextPath() %>/admin/orders">
            <label>Da data:</label>
            <input type="date" name="fromDate" value="<%= fromDate != null ? fromDate : "" %>">

            <label>A data:</label>
            <input type="date" name="toDate" value="<%= toDate != null ? toDate : "" %>">

            <label>Email cliente:</label>
            <input type="text" name="email" placeholder="Inserisci email"
                   value="<%= email != null ? email : "" %>">

            <button type="submit">Filtra</button>
        </form>
    </div>

    <div class="catalog-table-container">
        <table class="catalog-table">
            <tr>
                <th>Numero Ordine</th>
                <th>Email Cliente</th>
                <th>Fumetto</th>
                <th>Quantità</th>
                <th>Prezzo (€)</th>
                <th>Subtotale (€)</th>
                <th>Metodo Pagamento</th>
                <th>Totale Ordine (€)</th>
                <th>Data Ordine</th>
                <th>Stato</th>
                <th>Azioni</th>
            </tr>

            <%
                if (error != null && error) {
            %>
            <tr><td colspan="11">Errore durante il caricamento degli ordini.</td></tr>
            <%
                } else if (orders == null || orders.isEmpty()) {
            %>
            <tr><td colspan="11">Nessun ordine trovato per i criteri selezionati.</td></tr>
            <%
                } else {
                    for (Order order : orders) {
                        for (OrderItem item : order.getItems()) {
                            double subtotal = item.getQuantity() * item.getPrice();
            %>
            <tr>
                <td><%= order.getOrderNumber() %></td>
                <td><%= order.getUser() %></td>
                <td><%= item.getTitle() %></td>
                <td><%= item.getQuantity() %></td>
                <td>€ <%= String.format("%.2f", item.getPrice()) %></td>
                <td>€ <%= String.format("%.2f", subtotal) %></td>
                <td><%= order.getPaymentMethod() %></td>
                <td>€ <%= String.format("%.2f", order.getTotalPrice()) %></td>
                <td><%= order.getOrderDate() %></td>

                <!-- Stato ordine -->
                <td>
                    <form action="<%= request.getContextPath() %>/admin/changeOrderStatus"
                          method="post">
                        <input type="hidden" name="orderNumber" value="<%= order.getOrderNumber() %>">
                        <select name="status">
                            <option value="In elaborazione"
                                <%= "In elaborazione".equals(order.getStatus()) ? "selected" : "" %>>
                                In elaborazione
                            </option>
                            <option value="Spedito"
                                <%= "Spedito".equals(order.getStatus()) ? "selected" : "" %>>
                                Spedito
                            </option>
                        </select>
                        <button type="submit" class="action-button">Aggiorna</button>
                    </form>
                </td>

                <!-- Annulla ordine -->
                <td>
                    <form action="<%= request.getContextPath() %>/admin/cancelOrder"
                          method="post"
                          onsubmit="return confirm('Sei sicuro di voler annullare questo ordine?');">
                        <input type="hidden" name="orderNumber" value="<%= order.getOrderNumber() %>">
                        <button type="submit" class="action-button" style="background-color:#cc0000;">
                            Annulla
                        </button>
                    </form>
                </td>
            </tr>
            <%
                        }
                    }
                }
            %>
        </table>
    </div>

</div>

</body>
</html>
