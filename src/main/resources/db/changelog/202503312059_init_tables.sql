CREATE SCHEMA tt;

CREATE TABLE tt.user
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR      NOT NULL,
    password_hash VARCHAR(128) NOT NULL,
    is_deleted    BOOLEAN                  DEFAULT FALSE,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP WITH TIME ZONE
);