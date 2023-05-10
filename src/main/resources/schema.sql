CREATE SCHEMA IF NOT EXISTS FILMORATE_SHEMA AUTHORIZATION sa;

create table IF NOT EXISTS FILMORATE_SHEMA.rate
(
    id_rate integer auto_increment primary key,
    rate_date nvarchar(255) not null,
    rate nvarchar(255) not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.genre
(
    id_genre integer auto_increment primary key,
    genre_Date nvarchar(255) not null,
    genre_rus  nvarchar(255) not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.films
(
    id_film integer auto_increment primary key,
    name varchar(50) not null,
    description varchar(200) not null,
    release_date date not null,
    duration int8 not null,
    id_rate integer not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.users
(
    id_user integer auto_increment primary key,
    login nvarchar(255) not null,
    email nvarchar(255) not null,
    birthday date not null,
    name nvarchar(255) not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.genre_set
(
    id integer auto_increment primary key,
    id_film integer not null,
    id_genre integer not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.likes_set
(
    id integer auto_increment primary key,
    id_film integer not null,
    id_user integer not null,
    constraint l_film FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film),
    constraint l_user FOREIGN KEY (id_user) REFERENCES FILMORATE_SHEMA.users (id_user)
);

create table IF NOT EXISTS FILMORATE_SHEMA.friends
(
    id integer auto_increment,
    id_user integer not null,
    id_friend integer not null,
    id_type integer not null
    // primary key(id_user, id_friend)
);

create table IF NOT EXISTS FILMORATE_SHEMA.type_friends
(
    id_type integer primary key not null,
    fri_type nvarchar(255) not null
);

CREATE UNIQUE INDEX IF NOT EXISTS FILMORATE_SHEMA.friends_index_0 ON FILMORATE_SHEMA.friends (id_user, id_friend);

ALTER TABLE FILMORATE_SHEMA.genre_set ADD FOREIGN KEY (id_genre) REFERENCES FILMORATE_SHEMA.genre (id_genre);

ALTER TABLE FILMORATE_SHEMA.films ADD FOREIGN KEY (id_rate) REFERENCES FILMORATE_SHEMA.rate (id_rate);

ALTER TABLE FILMORATE_SHEMA.friends ADD FOREIGN KEY (id_type) REFERENCES FILMORATE_SHEMA.type_friends (id_type);

ALTER TABLE FILMORATE_SHEMA.likes_set ADD FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film);

ALTER TABLE FILMORATE_SHEMA.genre_set ADD FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film);

ALTER TABLE FILMORATE_SHEMA.likes_set ADD FOREIGN KEY (id_user) REFERENCES FILMORATE_SHEMA.users (id_user);

ALTER TABLE FILMORATE_SHEMA.friends ADD FOREIGN KEY (id_user) REFERENCES FILMORATE_SHEMA.users (id_user);
