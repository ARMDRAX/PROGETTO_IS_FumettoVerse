<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Comic" %>
<%@ page import="java.util.List" %>
<%@ include file="header.jsp" %>


<link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css">
<div id="cart-message"></div>

<div class="container index-page">
    <h2 class="consigliati-title">Tutti i Fumetti</h2>

    <div class="comics">
    <%
        List<Comic> comics = (List<Comic>) request.getAttribute("comics");
        if (comics != null) {
            for (Comic c : comics) {
    %>
        <div class="comic">
            <img src="<%= request.getContextPath() %>/images/<%= c.getImage() %>" alt="<%= c.getTitle() %>" />
            <h3><%= c.getTitle() %></h3>
            <div class="comic-footer">
                <p class="price">€ <%= c.getPrice() %></p>
                <form class="add-to-cart-form" data-comic-id="<%= c.getId() %>">
                    <button type="submit" class="add-to-cart">Aggiungi al carrello</button>
                </form>
            </div>
        </div>
    <%
            }
        } else {
    %>
        <p>Errore nel caricamento dei fumetti.</p>
    <%
        }
    %>
    </div>
</div>

<script src="<%= request.getContextPath() %>/scripts/index.js"></script>

<%@ include file="footer.jsp" %>
