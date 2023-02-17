create table user
(
    uid        int auto_increment,
    u_name     varchar(45)   not null,
    u_password varchar(45)   not null,
    u_state    int default 0 not null,
    nike_name  varchar(30)   not null,
    type       int default 0 not null,
    constraint user_u_name_uindex
        unique (u_name),
    constraint user_uid_uindex
        unique (uid)
);

alter table user
    add primary key (uid);
