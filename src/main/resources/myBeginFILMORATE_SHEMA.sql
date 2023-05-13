// тестовые данные

INSERT INTO FILMORATE_SHEMA.films (id_rate, duration, release_date, description, name) VALUES (4, 10, '2010-10-10', 'первый', 'фильм1');

INSERT INTO FILMORATE_SHEMA.users (name, birthday, email, login) VALUES ('первыйЮ1', '1999-09-10', '1@111.11', '000' );


MERGE INTO FILMORATE_SHEMA.genre_set(id_genre, id_film)
    VALUES
        (1, 1)

MERGE INTO FILMORATE_SHEMA.likes_set(id_user, id_film)
    VALUES

INSERT INTO FILMORATE_SHEMA.friends(id_type, id_friend, id_user, id)
    VALUES
(1,1,3,1),
(2,2,4,2);

SELECT * FROM FILMORATE_SHEMA.users AS U LEFT JOIN FILMORATE_SHEMA.friends AS F ON (U.id_user = F.id_user)
         where f.id_type = 1 AND ( (f.ID_USER, f.ID_FRIEND) IN ((f.ID_FRIEND, F.ID_USER), (F.ID_USER, F.ID_FRIEND) );


SELECT * FROM FILMORATE_SHEMA.users AS U LEFT JOIN FILMORATE_SHEMA.friends AS F ON (U.id_user = F.id_user)
         where f.id_type = 1 AND (f.ID_USER, f.ID_FRIEND) IN ( (f.ID_FRIEND, F.ID_USER), (F.ID_USER, F.ID_FRIEND) );