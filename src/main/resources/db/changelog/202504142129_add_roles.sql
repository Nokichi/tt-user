CREATE TABLE tt.role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

INSERT INTO tt.role (id, name)
VALUES (1, 'MANAGER');
INSERT INTO tt.role (id, name)
VALUES (2, 'USER');

ALTER TABLE tt.user
    ADD COLUMN role_id INTEGER NOT NULL DEFAULT 2
        CONSTRAINT fk_user_role
            REFERENCES tt.role (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;