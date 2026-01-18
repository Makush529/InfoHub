create table users
(
    id       bigserial
        primary key,
    "user"   char(15) not null,
    user_age date
);

create unique index users_index
    on users ("user");

create table security
(
    id       bigserial
        primary key,
    login    char(10) not null,
    password char(10) not null,
    user_id  bigint   not null
        constraint security_fk
            references users
);

create unique index login_index
    on security (login);

create table posts
(
    id        bigserial
        primary key,
    post_name varchar(20) not null,
    title     text        not null,
    post_age  date,
    user_id   integer     not null
        constraint posts_fk
            references users
            on update cascade on delete cascade
);

create index fki_posts_fk
    on posts (user_id);