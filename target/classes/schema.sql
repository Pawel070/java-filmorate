CREATE SCHEMA IF NOT EXISTS FILMORATE_SHEMA AUTHORIZATION sa;

create table IF NOT EXISTS FILMORATE_SHEMA.director_list
(
    id_director integer auto_increment primary key,
    name_director nvarchar(255)
);

create table IF NOT EXISTS FILMORATE_SHEMA.rate
(
    id_rate integer primary key,
    name nvarchar(255)
);

create table IF NOT EXISTS FILMORATE_SHEMA.genre
(
    id integer primary key not null,
    name nvarchar(255) not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.films
(
    id_film integer auto_increment primary key,
    name_films varchar(50) not null,
    description varchar(200) not null,
    release_date date not null,
    duration int8 not null,
    id_rate integer not null,
    id_director integer
);

create table IF NOT EXISTS FILMORATE_SHEMA.users
(
    id_user integer auto_increment primary key,
    login nvarchar(255) not null,
    email nvarchar(255) not null,
    birthday date not null,
    name_user nvarchar(255) not null
);

create table IF NOT EXISTS FILMORATE_SHEMA.genre_set
(
    id_film integer,
    id integer
);

create table IF NOT EXISTS FILMORATE_SHEMA.director
(
    id_film integer primary key,
    id_director integer
);

create table IF NOT EXISTS FILMORATE_SHEMA.likes_set
(
    id_film integer,
    id_user integer
);

create table IF NOT EXISTS FILMORATE_SHEMA.friends
(
    id_user integer,
    id_friend integer,
    id_type integer
);

create table IF NOT EXISTS FILMORATE_SHEMA.type_friends
(
    id_type integer primary key,
    fri_type nvarchar(255)
);

create table IF NOT EXISTS FILMORATE_SHEMA.reviews
(
    id_review long auto_increment primary key,
    id_user integer not null,
    id_film integer not null,
    content nvarchar(1024) not null,
    is_positive boolean,
    usefull integer
);

create table IF NOT EXISTS FILMORATE_SHEMA.events
(
    event_id integer AUTO_INCREMENT primary key,
    user_id integer not null,
    timestamp bigint not null,
    entity_id integer not null,
    event_type varchar(255) not null,
    operation varchar(255) not null
);

CREATE UNIQUE INDEX IF NOT EXISTS FILMORATE_SHEMA.friends_index_0 ON FILMORATE_SHEMA.friends (id_user, id_friend);

CREATE UNIQUE INDEX IF NOT EXISTS FILMORATE_SHEMA.genre_index_0 ON FILMORATE_SHEMA.genre_set (id_film, id);

ALTER TABLE FILMORATE_SHEMA.reviews ADD FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.reviews ADD FOREIGN KEY (id_user) REFERENCES FILMORATE_SHEMA.users (id_user) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.genre_set ADD FOREIGN KEY (id) REFERENCES FILMORATE_SHEMA.genre (id) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.films ADD FOREIGN KEY (id_rate) REFERENCES FILMORATE_SHEMA.rate (id_rate) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.friends ADD FOREIGN KEY (id_type) REFERENCES FILMORATE_SHEMA.type_friends (id_type) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.genre_set ADD FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.friends ADD FOREIGN KEY (id_user) REFERENCES FILMORATE_SHEMA.users (id_user) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.likes_set ADD FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.likes_set ADD FOREIGN KEY (id_user) REFERENCES FILMORATE_SHEMA.users (id_user) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.director ADD FOREIGN KEY (id_film) REFERENCES FILMORATE_SHEMA.films (id_film) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.director ADD FOREIGN KEY (id_director) REFERENCES FILMORATE_SHEMA.director_list (id_director) ON DELETE CASCADE;

ALTER TABLE FILMORATE_SHEMA.events ADD FOREIGN KEY (user_id) REFERENCES FILMORATE_SHEMA.users(id_user) ON DELETE CASCADE;