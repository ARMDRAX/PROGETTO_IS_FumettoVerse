<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Aggiungi Fumetto</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<h2>Aggiungi Nuovo Fumetto</h2>

<div class="form-container">
    
    <form action="<%= request.getContextPath() %>/admin/addComic" method="post" enctype="multipart/form-data">

        <label for="title">Titolo:</label>
        <input type="text" id="title" name="title" required>

        <label for="price">Prezzo (€):</label>
        <input type="number" id="price" name="price" step="0.01" min="0" required>

        <label for="tipo">Tipo:</label>
        <select id="tipo" name="tipo" required>
            <option value="MANGA">MANGA</option>
            <option value="FUMETTO">FUMETTO</option>
        </select>

        <label for="image">Immagine:</label>
        <input type="file" id="image" name="image" accept="image/*" required>

        <button type="submit">Salva</button>
    </form>
<a href="<%= request.getContextPath() %>/admin/catalog" class="cancel-link">
    Annulla
</a>

</div>

</body>
</html>
