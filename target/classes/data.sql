MERGE INTO FILMORATE_SHEMA.rate(id_rate, name)
    VALUES
        (1, 'G'),
        (2, 'PG'),
        (3, 'PG14'),
        (4, 'R'),
        (5, 'NC17');

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
