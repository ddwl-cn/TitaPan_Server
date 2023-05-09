create table user
(
    uid               int auto_increment,
    u_name            varchar(45)                         not null,
    u_password        varchar(45)                         not null,
    u_state           int          default 0              not null,
    nike_name         varchar(30)                         not null,
    type              int          default 0              not null,
    email             varchar(45)                         null,
    avatar_url        varchar(300)                        null,
    age               int                                 null,
    mobilePhoneNumber varchar(45)                         null,
    area              varchar(100)                        null,
    sex               varchar(5)   default '保密'           null,
    work              varchar(300)                        null,
    hobby             varchar(500)                        null,
    design            varchar(500) default '这个用户很懒还没有留言~' null,
    constraint user_u_name_uindex
        unique (u_name),
    constraint user_uid_uindex
        unique (uid)
);

alter table user
    add primary key (uid);

INSERT INTO tita_pan.user (uid, u_name, u_password, u_state, nike_name, type, email, avatar_url, age, mobilePhoneNumber, area, sex, work, hobby, design) VALUES (7, 'ddwl', 'e10adc3949ba59abbe56e057f20f883e', 0, '我是lhf', 0, 'ddwl@enoiv.com', 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', 18, '16634866288', '中国/河南省/郑州市/金水区', '男', '学生', '乒乓 跑步 编程', '这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~这个用户很懒还没有留言~');
INSERT INTO tita_pan.user (uid, u_name, u_password, u_state, nike_name, type, email, avatar_url, age, mobilePhoneNumber, area, sex, work, hobby, design) VALUES (8, '201917325', 'e10adc3949ba59abbe56e057f20f883e', 0, '牛牛牛牛牛牛', 0, null, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', 0, null, null, '保密', null, null, '这个用户很懒还没有留言~');
INSERT INTO tita_pan.user (uid, u_name, u_password, u_state, nike_name, type, email, avatar_url, age, mobilePhoneNumber, area, sex, work, hobby, design) VALUES (9, 'dlddwl', 'e10adc3949ba59abbe56e057f20f883e', 0, 'admin', 1, null, null, 25, null, null, '保密', null, null, '这个用户很懒还没有留言~');