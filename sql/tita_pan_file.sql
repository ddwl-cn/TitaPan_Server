create table file
(
    fid          int auto_increment,
    md5_val      varchar(45)          not null,
    storage_path varchar(3000)        not null,
    f_name       varchar(300)         not null,
    upload_date  varchar(25)          not null,
    f_size       bigint               not null,
    public_file  tinyint(1) default 0 not null,
    preview_url  varchar(200)         null,
    constraint file_fid_uindex
        unique (fid),
    constraint file_fname_uindex
        unique (f_name)
)
    comment '文件实体';

alter table file
    add primary key (fid);