CREATE TABLE regexp_occurrences (
    id SERIAL PRIMARY KEY,
    pattern TEXT NOT NULL,
    description TEXT,
    surrounding_text TEXT NOT NULL,
    topic_id INTEGER NOT NULL,
    url TEXT,
    CONSTRAINT fk_topic
        FOREIGN KEY(topic_id)
        REFERENCES topics(id)
);