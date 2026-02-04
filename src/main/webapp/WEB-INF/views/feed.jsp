
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>InfoHub | Главная</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<!-- Простая шапка сайта -->
<nav style="width: 100%; background: white; padding: 10px 0; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 30px; display: flex; justify-content: center;">
    <div style="width: 600px; display: flex; justify-content: space-between; align-items: center;">
        <strong style="color: #4a90e2; font-size: 20px;">InfoHub</strong>
        <div>
            <c:choose>
                <c:when test="${not empty sessionScope.username}">
                    <span>Привет, <strong>${sessionScope.username}</strong></span>
                    <a href="/posts/create" style="margin-left: 15px; color: #4a90e2; text-decoration: none;">+ Пост</a>
                    <a href="/security/logout" style="margin-left: 15px; color: #e74c3c; text-decoration: none;">Выход</a>
                </c:when>
                <c:otherwise>
                    <a href="/security/login" style="color: #4a90e2; text-decoration: none;">Вход</a>
                    <a href="/security/registration" style="margin-left: 15px; color: #4a90e2; text-decoration: none;">Регистрация</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<div class="feed-container">
    <h2>Свежие новости</h2>
    <c:forEach var="post" items="${postsList}">
        <article class="post-item">
            <div class="post-header">
                <span>Автор: <span class="author-badge">${post.authorName}</span></span>
                <span>${post.postAge}</span>
            </div>
            <div class="post-title">${post.postTitle}</div>
            <div class="post-content">${post.text}</div>
        </article>
    </c:forEach>
</div>
</body>
</html>
