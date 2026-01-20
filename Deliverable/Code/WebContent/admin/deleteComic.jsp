<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String title = (String) request.getAttribute("title");
    int id = (Integer) request.getAttribute("id");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Elimina Fumetto</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<h2>Conferma Eliminazione</h2>

<div class="form-container">
    <p>Sei sicuro di voler eliminare il fumetto: <strong><%= title %></strong>?</p>

    <form action="<%= request.getContextPath() %>/admin/deleteComic" method="post">
        <input type="hidden" name="id" value="<%= id %>">
        <button type="submit">Elimina</button>
       <a href="<%= request.getContextPath() %>/admin/catalog" class="cancel-link">
    Annulla
</a>

    </form>
</div>

</body>
</html>
