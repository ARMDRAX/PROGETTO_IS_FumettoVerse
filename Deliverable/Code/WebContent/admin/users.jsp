<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="control.AdminUsersServlet.SimpleUser" %>

<%
    String role = (String) session.getAttribute("role");
    if (role == null || !"ADMIN_USERS".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // filtro email
    String email = (String) request.getAttribute("email");
%>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Utenti - FumettoVerse</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/adminstyle.css">
</head>
<body>

<header>
    <div class="logo">FumettoVerse Gestore - Utenti</div>
</header>

<h2>Gestione Utenti</h2>

<div class="button-container" style="margin-bottom:20px;">
    <a href="<%= request.getContextPath() %>/admin/admin.jsp" class="action-button">
        Torna al pannello gestore
    </a>
</div>

<%
    String msg = (String) request.getAttribute("message");
    String err = (String) request.getAttribute("errorMessage");
    if (msg != null) {
%>
<p style="color:green; text-align:center; font-weight:bold;"><%= msg %></p>
<%
    } else if (err != null) {
%>
<p style="color:red; text-align:center; font-weight:bold;"><%= err %></p>
<%
    }
%>

<!-- Ricerca per email -->
<div class="form-container">
    <form method="get" action="<%= request.getContextPath() %>/admin/users">
        <label>Email:</label>
        <input type="text" name="email" placeholder="Cerca per email"
               value="<%= email != null ? email : "" %>">
        <button type="submit">Cerca</button>
    </form>
</div>

<%
    List<SimpleUser> users = (List<SimpleUser>) request.getAttribute("users");
    if (users == null || users.isEmpty()) {
%>
    <p style="text-align:center;">Nessun utente trovato.</p>
<%
    } else {
%>
<div class="catalog-table-container">
    <table class="catalog-table">
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Email</th>
            <th>Admin</th>
            <th>Ruolo</th>
            <th>Azioni</th>
        </tr>

        <%
            for (SimpleUser u : users) {
        %>
        <tr>
            <td>
                <%= u.getId() %>
            </td>

            <td>
                <form action="<%= request.getContextPath() %>/admin/editUser" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= u.getId() %>"/>
                    <input type="text" name="name" value="<%= u.getName() %>" required/>
            </td>

            <td>
                    <input type="email" name="email" value="<%= u.getEmail() %>" required/>
            </td>

            <td><%= u.isAdmin() ? "Sì" : "No" %></td>
            <td><%= u.getRole() %></td>

            <td>
                    <button type="submit" class="action-button">Salva</button>
                </form>

                <form action="<%= request.getContextPath() %>/admin/deleteUser" method="post"
                      style="display:inline;"
                      onsubmit="return confirm('Sei sicuro di voler eliminare questo utente?');">
                    <input type="hidden" name="id" value="<%= u.getId() %>"/>
                    <button type="submit" class="action-button" style="background-color:#cc0000;">
                        Elimina
                    </button>
                </form>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</div>
<%
    }
%>

</body>
</html>
