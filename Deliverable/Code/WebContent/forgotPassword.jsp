<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<title>Recupero password - FumettoVerse</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/loginelogoutstyle.css">

<%@ include file="header.jsp" %>

<h2>Recupero password</h2>

<% String step = request.getParameter("step"); %>

<% if (step == null || !"question".equals(step)) { %>
    <!-- STEP 1: inserisci email -->
    <form action="forgotPassword" method="post" class="login-form">
        <input type="hidden" name="action" value="emailStep">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">
        <button type="submit">Continua</button>
    </form>
<% } else { %>
    <!-- STEP 2: mostra domanda e chiedi risposta + nuova password -->
    <form action="forgotPassword" method="post" class="login-form">
        <input type="hidden" name="action" value="resetStep">
        <input type="hidden" name="email" value="<%= request.getAttribute("email") %>">

        <p><strong>Domanda di sicurezza:</strong>
            <%= request.getAttribute("securityQuestionText") %>
        </p>

        <label for="securityAnswer">Risposta</label>
        <input type="text" id="securityAnswer" name="securityAnswer" required>

        <label for="newPassword">Nuova password</label>
        <input type="password" id="newPassword" name="newPassword" required>

        <button type="submit">Reimposta password</button>
    </form>
<% } %>

<% String error = (String) request.getAttribute("error"); 
   String message = (String) request.getAttribute("message");
   if (error != null) { %>
    <p style="color:red; text-align:center; font-weight:bold;"><%= error %></p>
<% } else if (message != null) { %>
    <p style="color:green; text-align:center; font-weight:bold;"><%= message %></p>
<% } %>

<%@ include file="footer.jsp" %>
