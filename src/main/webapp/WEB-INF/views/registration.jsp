<%--
  Created by IntelliJ IDEA.
  User: evmak
  Date: 27.01.2026
  Time: 18:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h2>Registration Form</h2>
<form action="/security/registration" method="post">
    <div>
        <label for="login">Login:</label>
        <input type="text" id="login" name="login" required>
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
    </div>
    <div>
        <label for="firstname">First Name:</label>
        <input type="text" id="firstname" name="firstname" required>
    </div>
    <div>
        <label for="age">Age:</label>
        <input type="date" id="age" name="age" required>
    </div>
    <button type="submit">Register</button>
</form>
</body>
</html>