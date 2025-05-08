CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    student_id VARCHAR(8) UNIQUE,
    
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id)
);
