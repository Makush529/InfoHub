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
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 50px;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        form {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            width: 300px;
        }

        form div {
            margin-bottom: 15px;
        }

        label {
            display: block;
            font-weight: 600;
            margin-bottom: 5px;
            color: #666;
        }

        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box; /* Чтобы padding не раздувал ширину */
        }

        input:focus {
            border-color: #4a90e2;
            outline: none;
            box-shadow: 0 0 5px rgba(74,144,226,0.3);
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #4a90e2;
            border: none;
            border-radius: 4px;
            color: white;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover {
            background-color: #357abd;
        }
    </style>
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
        <label for="birthDate">Birth Date:</label>
        <input type="date" id="birthDate" name="birthDate" required>
    </div>
    <button type="submit">Register</button>
</form>
</body>
</html>