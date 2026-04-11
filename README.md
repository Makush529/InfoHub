# InfoHub

Информационно-развлекательная платформа с пользовательским контентом (UGC). Пользователи могут создавать посты, комментировать, ставить лайки/дизлайки и получать рейтинг.

## Технологический стек

- Java 21
- Spring Boot 3.5
- Spring Security + JWT
- BCrypt
- PostgreSQL
- Flyway
- Swagger/OpenAPI
- Maven
- Lombok
- SLF4J + AOP

## Архитектура 

<img width="509" height="553" alt="Снимок экрана 2026-04-11 145509" src="https://github.com/user-attachments/assets/f8fe505f-2aea-41fe-8406-d295c1253ce2" />

## Роли

| Роль | Права |
|------|-------|
| USER | Создание постов, комментариев, лайки |
| MODERATOR | USER + модерация |
| ADMIN | Полный доступ |

## API

### Авторизация (/auth)

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| POST | /auth/register | Регистрация | Все |
| POST | /auth/login | Вход (JWT) | Все |
| POST | /auth/logout | Выход | Авторизованные |
| GET | /auth/me | Текущий пользователь | Авторизованные |

### Посты (/posts)

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| GET | /posts | Все посты | Все |
| GET | /posts/{id} | Пост по ID | Все |
| POST | /posts | Создать пост | Авторизованные |
| PUT | /posts/{id} | Редактировать | Автор/Модератор |
| DELETE | /posts/{id} | Удалить пост | Автор/Модератор |
| POST | /posts/{id}/like | Лайк | Авторизованные |
| POST | /posts/{id}/dislike | Дизлайк | Авторизованные |
| DELETE | /posts/{id}/reaction | Убрать реакцию | Авторизованные |

### Комментарии (/comments)

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| GET | /comments/post/{postId} | Комментарии | Все |
| POST | /comments | Создать | Авторизованные |
| DELETE | /comments/{id} | Удалить | Автор/Модератор |

### Теги

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| GET | /tags | Все теги | Все |
| GET | /posts/tag/{tagName} | Посты по тегу | Все |

### Админка (/admin)

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| GET | /admin/posts/pending | Посты на модерацию | MODERATOR/ADMIN |
| POST | /admin/posts/{id}/approve | Одобрить пост | MODERATOR/ADMIN |
| POST | /admin/posts/{id}/reject | Отклонить пост | MODERATOR/ADMIN |
| GET | /admin/comments/pending | Комментарии на модерацию | MODERATOR/ADMIN |
| POST | /admin/comments/{id}/approve | Одобрить комментарий | MODERATOR/ADMIN |
| POST | /admin/comments/{id}/reject | Отклонить комментарий | MODERATOR/ADMIN |

## База данных

- users
- security
- user_roles
- posts
- comments
- tags
- post_tags
- user_likes
- user_dislikes
- logs
