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
delete from FILMORATE_SHEMA.EVENTS where EVENTS.EVENT_ID > 0;

ALTER TABLE FILMORATE_SHEMA.FILMS ALTER COLUMN ID_FILM RESTART WITH 1;
ALTER TABLE FILMORATE_SHEMA.USERS ALTER COLUMN ID_USER RESTART WITH 1;
ALTER TABLE FILMORATE_SHEMA.REVIEWS ALTER COLUMN ID_REVIEW RESTART WITH 1;
ALTER TABLE FILMORATE_SHEMA.EVENTS ALTER COLUMN EVENT_ID RESTART WITH 1;
ALTER TABLE FILMORATE_SHEMA.DIRECTOR_LIST ALTER COLUMN ID_DIRECTOR RESTART WITH 1;

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

INSERT INTO FILMORATE_SHEMA.director_list(name_director)
    VALUES
        ('Иванов'),
        ('Петров'),
        ('Сидоров' ),
        ('Шнипперсон');

