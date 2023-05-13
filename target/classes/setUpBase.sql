// загрузка базы тестовыми данными setUpBase.sql
INSERT INTO FILMORATE_SHEMA.films(name_films, description, release_date, duration, id_rate)
VALUES ('ИмФП1', 'Фильм1', '1901-01-01', 10, 1);
INSERT INTO FILMORATE_SHEMA.films(name_films, description, release_date, duration, id_rate)
VALUES ('ИмяФ2', 'Фильм2', '1902-02-02', 20, 2);
INSERT INTO FILMORATE_SHEMA.films(name_films, description, release_date, duration, id_rate)
VALUES ('ИмяФ3', 'Фильм3', '1903-03-03', 30, 3);
INSERT INTO FILMORATE_SHEMA.films(name_films, description, release_date, duration, id_rate)
VALUES ('ИмяФ4', 'Фильм4', '1904-04-04', 40, 4);

INSERT INTO FILMORATE_SHEMA.users(login, email, birthday, name_user)
VALUES ('ИмяП1', 'user1@test.ru', '1971-01-01', 'Пользователь1');
INSERT INTO FILMORATE_SHEMA.users(login, email, birthday, name_user)
VALUES ('ИмяП2', 'user2@test.ru', '1972-01-01', 'Пользователь2');
INSERT INTO FILMORATE_SHEMA.users(login, email, birthday, name_user)
VALUES ('ИмяП3', 'user3@test.ru', '1973-01-01', 'Пользователь3');
INSERT INTO FILMORATE_SHEMA.users(login, email, birthday, name_user)
VALUES ('ИмяП4', 'user4@test.ru', '1974-01-01', 'Пользователь4');

INSERT INTO FILMORATE_SHEMA.genre_set(id_film, id_genre)
VALUES (1, 1);
INSERT INTO FILMORATE_SHEMA.genre_set(id_film, id_genre)
VALUES (2, 2);
INSERT INTO FILMORATE_SHEMA.genre_set(id_film, id_genre)
VALUES (3, 3);
INSERT INTO FILMORATE_SHEMA.genre_set(id_film, id_genre)
VALUES (4, 4);

INSERT INTO FILMORATE_SHEMA.likes_set(id_film, id_user)
VALUES (1, 1);
INSERT INTO FILMORATE_SHEMA.likes_set(id_film, id_user)
VALUES (2, 2);
INSERT INTO FILMORATE_SHEMA.likes_set(id_film, id_user)
VALUES (2, 3);


INSERT INTO FILMORATE_SHEMA.friends(id_user, id_friend, id_type)
VALUES (1, 4, 1);
INSERT INTO FILMORATE_SHEMA.friends(id_user, id_friend, id_type)
VALUES (1, 3, 3);
INSERT INTO FILMORATE_SHEMA.friends(id_user, id_friend, id_type)
VALUES (2, 2, 1);

