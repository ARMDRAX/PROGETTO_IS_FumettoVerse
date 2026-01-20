<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String role = (String) session.getAttribute("role");
    if (role == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Admin Panel</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<header>
    <div class="logo">FumettoVerse Gestore</div>
</header>

<h2>Benvenuto Gestore</h2>

<div class="admin-image-container">
    <img src="<%= request.getContextPath() %>/images/admin.jpg" alt="Benvenuto Gestore">
</div>

<nav>
    <ul>
        <% if ("ADMIN_CATALOG".equals(role)) { %>
            <li><a href="<%= request.getContextPath() %>/admin/catalog">Gestione Catalogo</a></li>
        <% } %>

        <% if ("ADMIN_ORDERS".equals(role)) { %>
            <li><a href="<%= request.getContextPath() %>/admin/orders">Visualizza Ordini</a></li>
        <% } %>

        <% if ("ADMIN_USERS".equals(role)) { %>
            <li><a href="<%= request.getContextPath() %>/admin/users">Gestione Utenti</a></li>
        <% } %>

        <li><a href="<%= request.getContextPath() %>/logout">Logout Gestore</a></li>
    </ul>
</nav>

</body>
</html>
