CREATE TABLE facts (
                       id SERIAL PRIMARY KEY,
                       occurrence_id BIGINT NOT NULL,
                       inferred_text TEXT NOT NULL,
                       topic_id INTEGER NOT NULL,
                       CONSTRAINT fk_occurrence
                           FOREIGN KEY(occurrence_id)
                               REFERENCES regexp_occurrences(id),
                       CONSTRAINT fk_topic
                           FOREIGN KEY(topic_id)
                               REFERENCES topics(id)
);