create table mark
(
    mid   int auto_increment,
    uid   int not null,
    fid   int not null,
    score int not null,
    constraint mark_mid_uindex
        unique (mid)
);

alter table mark
    add primary key (mid);

INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (1, 7, 822, 4);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (2, 8, 822, 3);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (3, 7, 821, 5);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (4, 8, 821, 4);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (5, 9, 821, 5);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (6, 7, 823, 5);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (7, 7, 824, 1);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (8, 7, 814, 5);
INSERT INTO tita_pan.mark (mid, uid, fid, score) VALUES (9, 7, 812, 3);