ALTER TABLE users ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE users SET email_verified = TRUE
WHERE id_user IN (
    SELECT u.id_user FROM users u
    JOIN user_roles ur ON ur.user_id = u.id_user
    JOIN role r ON ur.role_id = r.id_role
    WHERE r.name != 'STUDENT'
);

CREATE TABLE email_verification_token (
    id         SERIAL PRIMARY KEY,
    token      VARCHAR(36) NOT NULL UNIQUE,
    user_id    INTEGER NOT NULL REFERENCES users(id_user) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    used       BOOLEAN NOT NULL DEFAULT FALSE
);
