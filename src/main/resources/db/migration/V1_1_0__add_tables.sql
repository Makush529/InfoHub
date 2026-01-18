create table comments
(
    id           bigserial not null,
    content      text,
    comment_date timestamp,
    post_id      integer
        constraint comments_posts___fk
            references posts
            on update cascade on delete cascade,
    user_id      integer
        constraint comments_users___fk
            references users
);

create table user_likes
(
    id      bigserial not null
        constraint user_likes_pk
            primary key,
    post_id integer   not null
        constraint user_likes_posts___fk
            references posts
            on update cascade on delete cascade,
    user_id integer
        constraint user_likes_users___fk
            references users
            on update cascade on delete cascade
);

create table user_dislikes
(
    id      bigserial
        constraint user_dislikes_pk
            primary key,
    post_id integer not null
        constraint user_dislikes_posts___fk
            references posts
            on update cascade on delete cascade,
    user_id integer
        constraint user_dislikes_users___fk
            references users
);

create table tags
(
    id       bigserial   not null
        constraint tags_pk
            primary key,
    tag_name varchar(50) not null
);

create table post_tags
(
    id      bigserial not null
        constraint post_tags_pk
            primary key,
    post_id integer   not null
        constraint post_tags_posts___fk
            references posts
            on update cascade on delete cascade,
    tag_id  integer   not null
        constraint post_tags_tags___fk
            references tags
            on update cascade on delete cascade
);

alter table comments
    add constraint comments_pk
        primary key (id);

create table public.user_roles
(
    id      bigserial
        constraint user_roles_pk
            primary key,
    user_id integer     not null
        constraint user_roles_users___fk
            references public.users
            on update cascade on delete cascade,
    role    varchar(15) not null
);

create table logs
(
    id         bigserial
        constraint logs_pk
            primary key,
    user_id    integer not null
        constraint logs_users___fk
            references users,
    action     varchar,
    details    text,
    created_at timestamp
);


