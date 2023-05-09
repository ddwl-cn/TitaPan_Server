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

INSERT INTO tita_pan.share_link (share_id, uid, fid, share_uuid, share_code, share_date, expire_date, valid, isFolder, storage_path, f_name) VALUES (54, 7, 811, '84bbdac6-47b9-4f9e-a648-24f257810afd', '111111', '2023-05-09 20:17', '2123-04-15 20:17', '永久', 0, '/', 'default_avatar.3420d7f1.jpg');