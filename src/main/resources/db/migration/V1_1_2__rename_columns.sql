alter table users
    rename column username to first_name;

alter table security
    add username varchar not null;

create unique index username__index
    on security (username);

alter table security
drop column username;

alter table users
    rename column first_name to username;

alter table posts
alter column user_id type bigint using user_id::bigint;

alter table posts
alter column post_name type varchar(255) using post_name::varchar(255);

alter table posts
    rename column post_name to post_title;

alter table posts
    rename column title to text;

alter table security
alter column login type char(20) using login::char(20);

alter table security
alter column password type char(20) using password::char(20);

ALTER TABLE posts ADD COLUMN status VARCHAR(20) DEFAULT 'PENDING';