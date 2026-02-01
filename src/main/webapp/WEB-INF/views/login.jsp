<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<h2>Login Form</h2>

<% if (request.getAttribute("error") != null) { %>
<div class="error-msg">${error}</div>
<% } %>

<form action="/security/login" method="post" class="auth-form">
    <div>
        <label for="login">Login:</label>
        <input type="text" id="login" name="login" required>
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
    </div>
    <button type="submit" class="btn-submit">Sign In</button>

    <div class="switch-link">
        Don't have an account? <a href="/security/registration">Register here</a>
    </div>
</form>
</body>
</html>
