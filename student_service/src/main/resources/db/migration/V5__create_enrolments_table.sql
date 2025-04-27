CREATE TABLE enrolments (
  id BIGSERIAL PRIMARY KEY,
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_enrolment_student FOREIGN KEY (student_id) REFERENCES students(id),
  CONSTRAINT fk_enrolment_course FOREIGN KEY (course_id) REFERENCES courses(id),
  CONSTRAINT uq_enrolment_student_course UNIQUE (student_id, course_id)
);
