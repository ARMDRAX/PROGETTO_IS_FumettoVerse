<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<title>Login - FumettoVerse</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/loginelogoutstyle.css">

<%@ include file="header.jsp" %>

<h2 id="idh2">Login</h2>

<div id="errorMessages"></div>

<form id="loginForm" class="login-form"
      action="${pageContext.request.contextPath}/login" method="post" novalidate>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">

    <label for="password">Password</label>
    <input type="password" id="password" name="password" required placeholder="Inserisci la tua password">

    <button type="submit">Accedi</button>

    <p style="text-align:center; margin-top: 15px;">
        Non hai un account?
        <a href="${pageContext.request.contextPath}/register.jsp"
           style="color: #e60000; font-weight: bold;">Registrati</a>
    </p>

    <p style="text-align:center; margin-top: 10px;">
        <a href="${pageContext.request.contextPath}/forgotPassword.jsp"
           style="color: #e60000; font-weight: bold;">Password dimenticata?</a>
    </p>
</form>

<%
    String error = request.getParameter("error");
    if ("1".equals(error)) {
%>
    <p style="color:red; text-align:center; font-weight:bold;">Email o password errati</p>
<%
    }
%>

<%@ include file="footer.jsp" %>
<script src="scripts/login.js"></script>
