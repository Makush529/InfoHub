<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Its profile</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>

<div class="auth-form profile-card">
    <div class="welcome-msg">Добро пожаловать,</div>
    <div class="username-highlight">${username}</div>

    <div class="status-badge">Авторизован</div>

    <div class="divider"></div>

    <p style="color: #666; font-size: 14px; margin-bottom: 25px;">
        Рады видеть вас снова в системе <strong>InfoHub</strong>.
        Теперь вам доступны все функции личного кабинета.
    </p>

    <%-- Ссылка на Logout, стилизованная под кнопку --%>
    <a href="/security/logout" class="btn-logout">Выйти из аккаунта</a>
</div>

</body>
</html>