CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(20) NOT NULL,
                                     email VARCHAR(50) NOT NULL,
                                     password VARCHAR(120) NOT NULL,
                                     register_date VARCHAR(120) NOT NULL,
                                     last_login VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id INTEGER NOT NULL,
                                          role_id INTEGER NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
                                          CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
                                          CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE refresh_token (
                               id SERIAL PRIMARY KEY,
                               user_id INTEGER NOT NULL,
                               refresh_token VARCHAR(255) NOT NULL UNIQUE,
                               expiry_date TIMESTAMP NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users (id)
);

DO
$do$
    BEGIN
        IF NOT EXISTS (SELECT FROM roles) THEN
            INSERT INTO roles(name) VALUES('ROLE_USER'), ('ROLE_MODERATOR'), ('ROLE_ADMIN');
        END IF;
    END
$do$
