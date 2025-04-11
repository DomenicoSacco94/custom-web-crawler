CREATE TABLE regexp_occurrences (
    id SERIAL PRIMARY KEY,
    regexp_id INTEGER NOT NULL,
    surrounding_text TEXT NOT NULL,
    url TEXT,
    CONSTRAINT fk_regexps
        FOREIGN KEY(regexp_id)
        REFERENCES regexps(id)
);