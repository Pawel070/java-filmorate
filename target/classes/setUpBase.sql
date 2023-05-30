// загрузка базы тестовыми данными setUpBase.sql

delete from FILMORATE_SHEMA.rate where RATE.ID_RATE > 0;
delete from FILMORATE_SHEMA.LIKES_SET where LIKES_SET.ID_USER > 0;
delete from FILMORATE_SHEMA.GENRE_SET where GENRE_SET.ID > 0;
delete from FILMORATE_SHEMA.GENRE  where GENRE.ID > 0;
delete from FILMORATE_SHEMA.TYPE_FRIENDS where TYPE_FRIENDS.ID_TYPE > 0;
delete from FILMORATE_SHEMA.FRIENDS where FRIENDS.ID_USER > 0;
delete from FILMORATE_SHEMA.FILMS where FILMS.ID_FILM > 0;
delete from FILMORATE_SHEMA.USERS where USERS.ID_USER > 0;
delete from FILMORATE_SHEMA.REVIEWS where REVIEWS.ID_REVIEW > 0;
delete from FILMORATE_SHEMA.DIRECTOR where DIRECTOR.ID_DIRECTOR > 0;
delete from FILMORATE_SHEMA.DIRECTOR_LIST where DIRECTOR_LIST.ID_DIRECTOR > 0;

ALTER TABLE FILMORATE_SHEMA.FILMS ALTER COLUMN ID_FILM RESTART WITH 1;
ALTER TABLE FILMORATE_SHEMA.USERS ALTER COLUMN ID_USER RESTART WITH 1;
ALTER TABLE FILMORATE_SHEMA.REVIEWS ALTER COLUMN ID_REVIEW RESTART WITH 1;

INSERT INTO FILMORATE_SHEMA.rate(id_rate, name)
    VALUES
        (1, 'G'),
        (2, 'PG'),
        (3, 'PG-13'),
        (4, 'R'),
        (5, 'NC-17');

INSERT INTO FILMORATE_SHEMA.genre(id, name)
    VALUES
        (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Триллер'),
        (5, 'Документальный'),
        (6, 'Боевик');

INSERT INTO FILMORATE_SHEMA.type_friends(id_type, fri_type)
    VALUES
        (1, 'FRIEND'),
        (2, 'NO_FRIEND'),
        (3, 'FRIEND_REQUEST');

INSERT INTO FILMORATE_SHEMA.director_list(id_director, name_director)
    VALUES
        (1, 'Иванов'),
        (2, 'Петров'),
        (3, 'Сидоров' ),
        (4, 'Шнипперсон');


INSERT INTO FILMORATE_SHEMA.films(name_films, description, release_date, duration, id_rate, id_director)
VALUES
    ('ИмФП1', 'Фильм1', '1901-01-01', 10, 1, 1),
    ('ИмяФ2', 'Фильм2', '1902-02-02', 20, 2, 2),
    ('ИмяФ3', 'Фильм3', '1903-03-03', 30, 3, 3),
    ('ИмяФ4', 'Фильм4', '1904-04-04', 40, 4, 4);

INSERT INTO FILMORATE_SHEMA.users(login, email, birthday, name_user)
VALUES
    ('ИмяП1', 'user1@test.ru', '1971-01-01', 'Пользователь1'),
    ('ИмяП2', 'user2@test.ru', '1972-01-01', 'Пользователь2'),
    ('ИмяП3', 'user3@test.ru', '1973-01-01', 'Пользователь3'),
    ('ИмяП4', 'user4@test.ru', '1974-01-01', 'Пользователь4');

INSERT INTO FILMORATE_SHEMA.genre_set(id_film, id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4);

INSERT INTO FILMORATE_SHEMA.likes_set(id_film, id_user)
VALUES
    (1, 1),
    (2, 2),
    (2, 3);


INSERT INTO FILMORATE_SHEMA.friends(id_user, id_friend, id_type)
VALUES
    (1, 4, 1),
    (1, 3, 3),
    (2, 2, 1);
