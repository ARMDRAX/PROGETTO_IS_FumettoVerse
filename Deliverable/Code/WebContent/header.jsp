<%@ page import="java.util.List" %>
<%
    String token = (String) session.getAttribute("authToken");
    String role = (String) session.getAttribute("role");
    String userName = (String) session.getAttribute("user");
    List<Integer> cart = (List<Integer>) session.getAttribute("cart");
    int cartSize = (cart != null) ? cart.size() : 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FumettoVerse</title>
    <link rel="stylesheet" href="styles/common.css">
    <link rel="stylesheet" href="styles/homepage.css">
</head>
<body>
<header>
    <div class="logo">FumettoVerse</div>
    <div class="user-links">
        <% if (token == null) { %>
            <a href="login.jsp" class="header-link">Login</a>
            <a href="register.jsp" class="header-link">Registrati</a>
        <% } else { %>
            <span class="welcome-text">Ciao, <%= userName %>!</span>
            <a href="logout" class="header-link">Logout</a>
        <% } %>
        <a href="cart" class="header-link cart-container">
            <img src="images/carrello.jpg" alt="Carrello" class="cart-icon" style="width: 60px; height: auto;" />
            <span class="cart-count"><%= cartSize %></span>
        </a>
    </div>
</header>

<nav class="nav-bar">
    <a href="index">Home</a>
    <a href="allcomics">Tutti i fumetti</a>
    <a href="manga">Manga</a>
    <a href="comics">Comics</a>
    <a href="myorders">I miei ordini</a>
    <% if ("admin".equals(role)) { %>
        <a href="admin/admin.jsp">Gestione catalogo</a>
    <% } %>
</nav>
