<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<h2>Registration Form</h2>

<form action="/security/registration" method="post" class="auth-form">
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
        <input type="text" id="username" name="username" required>
    </div>
    <div>
        <label for="birthDate">Birth Date:</label>
        <input type="date" id="birthDate" name="birthDate" required>
    </div>
    <button type="submit" class="btn-submit">Register</button>

    <div class="switch-link">
        Already have an account? <a href="/security/login">Login here</a>
    </div>
</form>
</body>
</html>
