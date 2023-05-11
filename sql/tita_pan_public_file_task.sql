create table public_file_task
(
    tid        int auto_increment,
    uid        int           not null,
    state      int default 0 not null,
    remark     varchar(500)  null,
    suggestion varchar(500)  null,
    fid        int           not null,
    constraint public_file_task_tid_uindex
        unique (tid)
);

alter table public_file_task
    add primary key (tid);

