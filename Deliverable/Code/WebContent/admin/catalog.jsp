<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Comic" %>
<%
    String role = (String) session.getAttribute("role");
    if (role == null || !"ADMIN_CATALOG".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    List<Comic> comics = (List<Comic>) request.getAttribute("comics");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Catalogo</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<header>
    <div class="logo">FumettoVerse - Gestore Catalogo</div>
</header>

<div class="container">

    <h2>Catalogo Fumetti</h2>

    <div class="button-container" style="margin-bottom:20px;">
        <a href="<%= request.getContextPath() %>/admin/admin.jsp"
           class="action-button">Torna al pannello gestore</a>
    </div>

    <% if ("1".equals(request.getAttribute("deleted"))) { %>
    <div class="success-message" id="delete-msg">
        ✅ Fumetto eliminato con successo.
    </div>
    <% } %>

    <% if ("1".equals(request.getParameter("added"))) { %>
        <div class="success-message" id="add-msg">
            ✅ Fumetto aggiunto con successo.
        </div>
    <% } %>

    <div class="button-container" style="margin-top:10px; margin-bottom:20px;">
        <a href="<%= request.getContextPath() %>/admin/addComic.jsp"
           class="action-button">Aggiungi Nuovo Fumetto</a>
    </div>

    <div class="catalog-table-container">
        <table class="catalog-table">
            <tr>
                <th>ID</th><th>Titolo</th><th>Prezzo</th><th>Azioni</th>
            </tr>

            <%  if (comics == null || comics.isEmpty()) { %>
            <tr><td colspan="4">Nessun fumetto presente.</td></tr>
            <%  } else {
                    for (Comic c : comics) { %>
            <tr>
                <td><%= c.getId() %></td>
                <td><%= c.getTitle() %></td>
                <td>€ <%= String.format("%.2f", c.getPrice()) %></td>
                <td>
                    <a href="<%= request.getContextPath()
                              %>/admin/editComic?id=<%= c.getId() %>"
                       class="action-button">Modifica</a>

                    <a href="<%= request.getContextPath()
                              %>/admin/deleteComic?id=<%= c.getId() %>"
                       class="action-button">Cancella</a>
                </td>
            </tr>
            <%      }
                } %>
        </table>
    </div>

</div>

<script>
window.addEventListener("DOMContentLoaded", () => {
    const msgs = [document.getElementById("delete-msg"), document.getElementById("add-msg")];

    msgs.forEach(msg => {
        if (msg) {
            setTimeout(() => {
                msg.style.opacity = '0';
                setTimeout(() => msg.remove(), 500);
            }, 3000);
        }
    });
});
</script>
</body>
</html>
