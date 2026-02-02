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

