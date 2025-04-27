CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id)
);
