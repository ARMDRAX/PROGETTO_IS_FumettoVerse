<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<title>Registrazione - FumettoVerse</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/loginelogoutstyle.css">

<%@ include file="header.jsp" %>

<h2 id="idh2">Registrazione</h2>

<div id="errorMessages"></div>

<form id="registerForm" class="login-form" action="register" method="post" novalidate>
    <label for="name">Nome</label>
    <input type="text" id="name" name="name" required placeholder="Inserisci il tuo nome">

    <label for="email">Email</label>
    <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">

    <label for="password">Password</label>
    <input type="password" id="password" name="password" required placeholder="Crea una password">

    <!-- Domanda di sicurezza -->
    <label for="securityQuestion">Domanda di sicurezza</label>
    <select id="securityQuestion" name="securityQuestion" required>
        <option value="">-- Seleziona una domanda --</option>
        <option value="pet">Nome del tuo primo animale domestico?</option>
        <option value="city">In che città sei nato?</option>
        <option value="school">Nome della tua scuola elementare?</option>
    </select>

    <label for="securityAnswer">Risposta</label>
    <input type="text" id="securityAnswer" name="securityAnswer"
           required placeholder="Inserisci la risposta segreta">

    <button type="submit">Registrati</button>

    <p style="text-align:center; margin-top: 15px;">
        Hai già un account?
        <a href="${pageContext.request.contextPath}/login.jsp"
           style="color: #e60000; font-weight: bold;">Accedi</a>
    </p>
</form>

<%
    String error = request.getParameter("error");
    if ("1".equals(error)) {
%>
    <p style="color:red; text-align:center; font-weight:bold;">Errore durante la registrazione, riprova.</p>
<%
    }
%>

<%@ include file="footer.jsp" %>
<script src="scripts/register.js"></script>
