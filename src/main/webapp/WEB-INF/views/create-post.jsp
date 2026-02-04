
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Создать пост | InfoHub</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<h2>Новая публикация</h2>

<form action="/posts/create" method="post" class="auth-form" style="width: 500px;">
    <div>
        <label for="postTitle">Заголовок новости:</label>
        <input type="text" id="postName" name="postTitle" placeholder="Броский заголовок..." required>
    </div>
    <div>
        <label for="text">Контент:</label>
        <textarea id="title" name="text" class="input-text" placeholder="Текст вашей новости..." required></textarea>
    </div>
    <button type="submit" class="btn-submit">Опубликовать на портале</button>

    <div class="switch-link">
        <a href="/feed">← Назад к свежему</a>
    </div>
</form>
</body>
</html>
