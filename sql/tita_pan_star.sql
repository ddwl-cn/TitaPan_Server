create table star
(
    sid int auto_increment,
    uid int not null,
    cid int not null,
    constraint Star_sid_uindex
        unique (sid)
);

alter table star
    add primary key (sid);

INSERT INTO tita_pan.star (sid, uid, cid) VALUES (31, 7, 39);
INSERT INTO tita_pan.star (sid, uid, cid) VALUES (36, 7, 49);
INSERT INTO tita_pan.star (sid, uid, cid) VALUES (37, 7, 48);
INSERT INTO tita_pan.star (sid, uid, cid) VALUES (38, 7, 50);
INSERT INTO tita_pan.star (sid, uid, cid) VALUES (39, 7, 51);
INSERT INTO tita_pan.star (sid, uid, cid) VALUES (41, 7, 41);
INSERT INTO tita_pan.star (sid, uid, cid) VALUES (49, 7, 78);