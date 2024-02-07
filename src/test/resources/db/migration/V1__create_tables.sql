CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS relation_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

DO
$do$
    BEGIN
        IF NOT EXISTS (SELECT FROM roles) THEN
            INSERT INTO roles(name) VALUES('ROLE_USER'),
                                          ('ROLE_MODERATOR'),
                                          ('ROLE_ADMIN');
        END IF;
        IF NOT EXISTS (SELECT FROM relation_types) THEN
            INSERT INTO relation_types(name) VALUES('TYPE_FAVORITE'),
                                                   ('TYPE_READING'),
                                                   ('TYPE_READ'),
                                                   ('TYPE_TO_READ');
        END IF;
        IF NOT EXISTS (SELECT FROM genres) THEN
            INSERT INTO genres(name) VALUES('GENRE_FANTASY'),
                                           ('GENRE_ADVENTURE'),
                                           ('GENRE_ROMANCE'),
                                           ('GENRE_CONTEMPORARY'),
                                           ('GENRE_DYSTOPIAN'),
                                           ('GENRE_MYSTERY'),
                                           ('GENRE_HORROR'),
                                           ('GENRE_THRILLER'),
                                           ('GENRE_PARANORMAL'),
                                           ('GENRE_HISTORICAL_FICTION'),
                                           ('GENRE_SCIENCE_FICTION'),
                                           ('GENRE_CHILDREN'),
                                           ('GENRE_MEMOIR'),
                                           ('GENRE_COOKBOOK'),
                                           ('GENRE_ART'),
                                           ('GENRE_SELF_HELP'),
                                           ('GENRE_PERSONAL_DEVELOPMENT'),
                                           ('GENRE_MOTIVATIONAL'),
                                           ('GENRE_HEALTH'),
                                           ('GENRE_HISTORY'),
                                           ('GENRE_TRAVEL'),
                                           ('GENRE_GUIDE'),
                                           ('GENRE_RELATIONSHIPS'),
                                           ('GENRE_HUMOR');
        END IF;
    END
$do$
