CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS relation_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
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
        IF NOT EXISTS (SELECT FROM categories) THEN
            INSERT INTO categories(name) VALUES('CATEGORY_FANTASY'),
                                               ('CATEGORY_ADVENTURE'),
                                               ('CATEGORY_ROMANCE'),
                                               ('CATEGORY_CONTEMPORARY'),
                                               ('CATEGORY_DYSTOPIAN'),
                                               ('CATEGORY_MYSTERY'),
                                               ('CATEGORY_HORROR'),
                                               ('CATEGORY_THRILLER'),
                                               ('CATEGORY_PARANORMAL'),
                                               ('CATEGORY_HISTORICAL_FICTION'),
                                               ('CATEGORY_SCIENCE_FICTION'),
                                               ('CATEGORY_CHILDREN'),
                                               ('CATEGORY_MEMOIR'),
                                               ('CATEGORY_COOKBOOK'),
                                               ('CATEGORY_ART'),
                                               ('CATEGORY_SELF_HELP'),
                                               ('CATEGORY_PERSONAL_DEVELOPMENT'),
                                               ('CATEGORY_MOTIVATIONAL'),
                                               ('CATEGORY_HEALTH'),
                                               ('CATEGORY_HISTORY'),
                                               ('CATEGORY_TRAVEL'),
                                               ('CATEGORY_GUIDE'),
                                               ('CATEGORY_RELATIONSHIPS'),
                                               ('CATEGORY_HUMOR');
        END IF;
    END
$do$
