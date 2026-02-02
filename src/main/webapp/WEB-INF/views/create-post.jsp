
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
        <label for="postName">Заголовок новости:</label>
        <input type="text" id="postName" name="postName" placeholder="Броский заголовок..." required>
    </div>
    <div>
        <label for="title">Контент:</label>
        <textarea id="title" name="title" class="input-text" placeholder="Текст вашей новости..." required></textarea>
    </div>
    <button type="submit" class="btn-submit">Опубликовать на портале</button>

    <div class="switch-link">
        <a href="/">← Назад к свежему</a>
    </div>
</form>
</body>
</html>
