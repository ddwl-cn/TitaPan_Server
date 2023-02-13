create table share_link
(
    share_id     int auto_increment,
    uid          int           not null,
    fid          int           not null,
    share_uuid   varchar(200)  not null,
    share_code   varchar(15)   not null,
    share_date   varchar(45)   not null,
    expire_date  varchar(45)   not null,
    valid        varchar(10)   not null,
    isFolder     tinyint(1)    not null,
    storage_path varchar(3000) not null,
    f_name       varchar(300)  not null,
    constraint share_link_share_id_uindex
        unique (share_id),
    constraint share_link_share_link_uindex
        unique (share_uuid)
);

alter table share_link
    add primary key (share_id);

