MERGE INTO FILMORATE_SHEMA.rate(id_rate, rate_Date, rate)
    VALUES
        (1, 'G', 'G у фильма нет возрастных ограничений'),
        (2, 'PG', 'PG детям рекомендуется смотреть фильм с родителями'),
        (3, 'PG14', 'PG-13 детям до 13 лет просмотр не желателен'),
        (4, 'R', 'R лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
        (5, 'NC17', 'NC-17 лицам до 18 лет просмотр запрещён');

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

