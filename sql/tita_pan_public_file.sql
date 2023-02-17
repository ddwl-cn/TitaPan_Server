create table public_file
(
    fid           int              not null,
    f_name        varchar(300)     not null,
    n_name        varchar(300)     not null,
    hot           int    default 0 not null,
    f_size        bigint default 0 not null,
    f_description varchar(300)     null,
    upload_date   varchar(30)      not null,
    preview_url   varchar(300)     null,
    score         float  default 0 not null,
    constraint public_file_fid_uindex
        unique (fid)
)
    comment '公共文件表';