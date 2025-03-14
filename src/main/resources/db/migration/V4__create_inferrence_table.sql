CREATE TABLE inferences (
    id SERIAL PRIMARY KEY,
    occurrence_id BIGINT NOT NULL,
    inferred_text TEXT NOT NULL,
    CONSTRAINT fk_occurrence
        FOREIGN KEY(occurrence_id)
        REFERENCES regexp_occurrences(id)
);