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
    <div class="divider"></div>

    <!-- Кнопка перехода к форме создания поста -->
    <a href="/posts/create" class="btn-submit link-as-btn" style="width: 100%; box-sizing: border-box; text-decoration: none; display: inline-block; margin-bottom: 10px;">
        + Опубликовать новость
    </a>

    <!-- Кнопка перехода на главную ленту -->
    <a href="/" class="btn-secondary" style="display: block; text-decoration: none; padding: 10px; background: #f1f1f1; color: #555; border-radius: 4px; text-align: center; margin-bottom: 10px;">
        На главную страницу
    </a>
    <%-- Ссылка на Logout, стилизованная под кнопку --%>
    <a href="/security/logout" class="btn-logout">Выйти из аккаунта</a>
</div>

</body>
</html>