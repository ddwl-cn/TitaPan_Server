create table comment
(
    cid             int auto_increment,
    uid             int            not null,
    fid             int            not null,
    star            int default 0  not null,
    comment_date    varchar(45)    not null,
    content         varchar(2000)  not null,
    reply_to        int default -1 not null,
    avatar_url      varchar(200)   null,
    nike_name       varchar(45)    not null,
    reply_num       int default 0  not null,
    belong_to       int default -1 not null,
    reply_nike_name varchar(45)    null,
    constraint comment_cid_uindex
        unique (cid)
);

alter table comment
    add primary key (cid);

INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (39, 7, 821, 1, '2023-05-04 23:46', '666', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 4, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (40, 8, 821, 0, '2023-05-04 23:47', '5', 7, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '201917325', 0, 39, '我是lhf');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (41, 8, 821, 1, '2023-05-04 23:47', '对对对', 8, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '201917325', 0, 39, '201917325');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (42, 7, 821, 0, '2023-05-05 00:00', '赞', 8, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 0, 39, '201917325');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (43, 8, 821, 0, '2023-05-05 00:01', 'wireshark yyds！', -1, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '201917325', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (44, 7, 821, 0, '2023-05-05 00:02', '?', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (45, 7, 821, 0, '2023-05-05 00:03', '六?', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (46, 7, 821, 0, '2023-05-05 00:06', '?', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (47, 8, 821, 0, '2023-05-05 00:06', '?', -1, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '201917325', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (48, 7, 822, 1, '2023-05-05 12:15', '用着怎么样？', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 1, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (49, 8, 822, 1, '2023-05-05 12:16', '运行报错啊，有没有详细安装步骤？', 7, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '牛牛牛牛牛牛', 0, 48, '我是lhf');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (50, 8, 822, 1, '2023-05-05 12:57', '6', -1, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '牛牛牛牛牛牛', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (51, 8, 822, 1, '2023-05-05 13:04', '1', -1, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '牛牛牛牛牛牛', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (52, 9, 821, 0, '2023-05-05 16:20', '6', -1, null, 'admin', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (57, 8, 826, 0, '2023-05-06 21:23', '666', -1, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '牛牛牛牛牛牛', 0, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (62, 7, 826, 0, '2023-05-06 22:54', '很好的资源，赞?', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 1, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (63, 8, 826, 0, '2023-05-07 22:37', '我的毕设就和屎一样', 7, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '牛牛牛牛牛牛', 0, 62, '我是lhf');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (77, 7, 856, 0, '2023-05-09 21:29', '为什么新版图吧工具箱我打不开(ROG枪神7)?', -1, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 2, -1, '');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (78, 8, 856, 1, '2023-05-09 21:45', '建议试试别的版本', 7, 'http://127.0.0.1:8012/BbUNOBZXOGR31UaFiytoaWhlBVaUHWsYSXecxDWLjXvEFTTsUnTP72dBgWXwlg4C-1683259974854-preview.jpg', '牛牛牛牛牛牛', 0, 77, '我是lhf');
INSERT INTO tita_pan.comment (cid, uid, fid, star, comment_date, content, reply_to, avatar_url, nike_name, reply_num, belong_to, reply_nike_name) VALUES (79, 7, 856, 0, '2023-05-09 21:46', '好吧?', 8, 'http://127.0.0.1:8012/xiI54sc5vYmhNQsdyFRv1mtS2sO6MxycI0jJ28W9Oo0HGrkfcu3C5u5gdCOqbFmo-1683507657092-preview.gif', '我是lhf', 0, 77, '牛牛牛牛牛牛');