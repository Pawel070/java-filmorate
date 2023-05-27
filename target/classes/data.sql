delete from FILMORATE_SHEMA.rate where RATE.ID_RATE > 0;
delete from FILMORATE_SHEMA.LIKES_SET where LIKES_SET.ID_USER > 0;
delete from FILMORATE_SHEMA.GENRE_SET where GENRE_SET.ID_GENRE > 0;
delete from FILMORATE_SHEMA.GENRE  where GENRE.ID_GENRE > 0;
delete from FILMORATE_SHEMA.TYPE_FRIENDS where TYPE_FRIENDS.ID_TYPE > 0;
delete from FILMORATE_SHEMA.FRIENDS where FRIENDS.ID_USER > 0;
delete from FILMORATE_SHEMA.FILMS where FILMS.ID_FILM > 0;
delete from FILMORATE_SHEMA.USERS where USERS.ID_USER > 0;

MERGE INTO FILMORATE_SHEMA.rate(id_rate, name)
    VALUES
        (1, 'G'),
        (2, 'PG'),
        (3, 'PG-13'),
        (4, 'R'),
        (5, 'NC-17');

MERGE INTO FILMORATE_SHEMA.genre(id_genre, genre_date, genre_rus)
    VALUES
        (1, 'COMEDY' , 'Комедия'),
        (2, 'DRAMA' , 'Драма'),
        (3, 'CARTOON' , 'Мультфильм'),
        (4, 'TRILLER' , 'Триллер'),
        (5, 'DOCUMENTARY' , 'Документальный'),
        (6, 'HITMAN' , 'Боевик'),
        (7, 'INDISTINCT', 'Неясный');

MERGE INTO FILMORATE_SHEMA.type_friends(id_type, fri_type)
    VALUES
        (1, 'FRIEND'),
        (2, 'NO_FRIEND'),
        (3, 'FRIEND_REQUEST');
