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

INSERT INTO tita_pan.user (uid, u_name, u_password, u_state, nike_name, type) VALUES (7, 'ddwl', 'e10adc3949ba59abbe56e057f20f883e', 0, '201917325', 0);
INSERT INTO tita_pan.user (uid, u_name, u_password, u_state, nike_name, type) VALUES (8, '201917325', 'e10adc3949ba59abbe56e057f20f883e', 0, 'DDWL', 0);
INSERT INTO tita_pan.user (uid, u_name, u_password, u_state, nike_name, type) VALUES (9, 'dlddwl', 'e10adc3949ba59abbe56e057f20f883e', 0, 'lhf', 1);