CREATE TABLE regexp_occurrences (
    id SERIAL PRIMARY KEY,
    pattern TEXT NOT NULL,
    description TEXT,
    surrounding_text TEXT NOT NULL,
    url TEXT
);