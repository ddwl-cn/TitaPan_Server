create table file
(
    fid           int auto_increment,
    md5_val       varchar(45)          not null,
    storage_path  varchar(3000)        not null,
    f_name        varchar(300)         not null,
    upload_date   varchar(25)          not null,
    f_size        bigint               not null,
    public_file   tinyint(1) default 0 not null,
    preview_url   varchar(200)         null,
    f_description varchar(300)         null,
    n_name        varchar(300)         null,
    constraint file_fid_uindex
        unique (fid),
    constraint file_fname_uindex
        unique (f_name)
)
    comment '文件实体';

alter table file
    add primary key (fid);

INSERT INTO tita_pan.file (fid, md5_val, storage_path, f_name, upload_date, f_size, public_file, preview_url, f_description, n_name) VALUES (620, 'd92878a4ac4d548da83202d631e3e361', 'D:/mySource/javaP/TitaPan/src/main/resources/sysFile/', 'd92878a4ac4d548da83202d631e3e361.mp4', '2023-02-17 00:14', 97324429, 1, 'http://127.0.0.1:8012/aEHUlQTd4U7AJiX768LuL4eypBEvnX0SDqbBNoEuLrY04tZvOgy1eKFUoKKcP7l0-1676564044942-preview.mp4', '1', 'level_3.mp4');