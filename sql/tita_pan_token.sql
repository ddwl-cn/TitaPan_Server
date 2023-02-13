create table token
(
    uid   int          not null,
    token varchar(200) not null,
    constraint token_token_uindex
        unique (token)
);

