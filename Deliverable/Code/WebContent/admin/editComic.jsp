<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    model.Comic comic = (model.Comic) request.getAttribute("comic");
    if (comic == null) {
        response.sendRedirect(request.getContextPath() + "/admin/catalog.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Modifica Fumetto</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<h2>Modifica Fumetto</h2>

<div class="form-container">
    <form action="<%= request.getContextPath() %>/admin/editComic" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="<%= comic.getId() %>">
        <input type="hidden" name="currentImage" value="<%= comic.getImage() %>">

        <label for="title">Titolo:</label>
        <input type="text" id="title" name="title" value="<%= comic.getTitle() %>" required>

        <label for="price">Prezzo (€):</label>
        <input type="number" id="price" name="price" value="<%= comic.getPrice() %>" step="0.01" min="0" required>

        <label for="tipo">Tipo:</label>
        <select id="tipo" name="tipo" required>
            <option value="MANGA" <%= "MANGA".equalsIgnoreCase(comic.getTipo()) ? "selected" : "" %>>Manga</option>
            <option value="FUMETTO" <%= "FUMETTO".equalsIgnoreCase(comic.getTipo()) ? "selected" : "" %>>Fumetto</option>
        </select>

        <p>Immagine attuale:</p>
        <img src="<%= request.getContextPath() %>/images/<%= comic.getImage() %>" alt="<%= comic.getTitle() %>" width="150" style="margin-bottom: 10px;" />

        <label for="image">Sostituisci immagine:</label>
        <input type="file" id="image" name="image" accept="image/*">

        <button type="submit">Aggiorna</button>
    </form>

  <a href="<%= request.getContextPath() %>/admin/catalog" class="cancel-link">
    Annulla
</a>

</div>

</body>
</html>
