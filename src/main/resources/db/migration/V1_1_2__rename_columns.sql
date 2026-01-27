alter table users
    rename column username to first_name;

alter table security
    add username varchar not null;

create unique index username__index
    on security (username);