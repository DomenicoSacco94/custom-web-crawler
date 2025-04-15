CREATE TABLE facts (
                       id SERIAL PRIMARY KEY,
                       occurrence_id BIGINT NOT NULL,
                       inferred_text TEXT NOT NULL,
                       consequences TEXT NOT NULL,
                       CONSTRAINT fk_occurrence
                           FOREIGN KEY(occurrence_id)
                               REFERENCES occurrences(id)
);