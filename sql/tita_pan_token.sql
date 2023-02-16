create table token
(
    uid   int          not null,
    token varchar(200) not null,
    constraint token_token_uindex
        unique (token)
);

INSERT INTO tita_pan.token (uid, token) VALUES (8, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1X25hbWUiOiIyMDE5MTczMjUiLCJ1aWQiOjgsInVzZXJQYXRoIjoiLyIsImV4cCI6MTY3NjkwMDUyOH0.inw9vnG4BIWyxG_NDXN7VCn4b8Y6jqebuSckwSOiTno');
INSERT INTO tita_pan.token (uid, token) VALUES (9, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1X25hbWUiOiJkbGRkd2wiLCJ1aWQiOjksInVzZXJQYXRoIjoiLyIsImV4cCI6MTY3NzE2MTA4OH0.GeiY3Vjs12WOEUe9ihsxQS0VuT-R6jQKsLTXw3WISAA');
INSERT INTO tita_pan.token (uid, token) VALUES (9, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1X25hbWUiOiJkbGRkd2wiLCJ1aWQiOjksInVzZXJQYXRoIjoiLyIsImV4cCI6MTY3NzE2MTAxOH0.qiQ_fZA2eqldsJ0oYdGug29rpXEY9B11fykZJACQWmk');
INSERT INTO tita_pan.token (uid, token) VALUES (7, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1X25hbWUiOiJkZHdsIiwidWlkIjo3LCJ1c2VyUGF0aCI6Ii8iLCJleHAiOjE2NzY4OTk3NjB9.t8TiKOd6cH7U6xWugDaHGJNGSXJACh7W3j0cGVo2CXk');