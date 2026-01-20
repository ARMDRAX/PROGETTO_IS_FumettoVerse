<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Comic" %>
<%@ page import="java.util.List" %>
<%@ include file="header.jsp" %>

<div id="cart-message"></div>
<div class="container index-page">
    <div class="hero-section">
        <img src="<%= request.getContextPath() %>/images/home.jpg" alt="Benvenuto su FumettoVerse" />
        <div class="welcome-overlay">Benvenuto!</div>
    </div>

    <h2 class="consigliati-title">Ultime uscite</h2>

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
        }
    %>
    </div>
</div>

<script src="<%= request.getContextPath() %>/scripts/index.js"></script>

<%@ include file="footer.jsp" %>