create table file_chunk
(
    id                 varchar(45)          not null,
    md5_val            varchar(45)          not null,
    chunk_size         bigint               not null,
    number             int                  not null,
    original_file_name varchar(300)         not null,
    total              int                  not null,
    storage_path       varchar(300)         not null,
    std_chunk_size     bigint               not null,
    suffix             varchar(15)          not null,
    upload_date        varchar(25)          not null,
    public_file        tinyint(1) default 0 not null,
    constraint file_chunk_md5_val_uindex
        unique (md5_val)
)
    comment '文件分块表';

alter table file_chunk
    add primary key (md5_val);

