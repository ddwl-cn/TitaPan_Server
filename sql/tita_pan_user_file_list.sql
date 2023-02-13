create table user_file_list
(
    uid          int                  not null,
    fid          int                  not null,
    upload_date  varchar(25)          not null,
    f_name       varchar(300)         not null,
    storage_path varchar(3000)        not null,
    f_size       bigint               null,
    isFolder     tinyint(1) default 0 not null,
    preview_url  varchar(200)         null
)
    comment '用户文件列表';

