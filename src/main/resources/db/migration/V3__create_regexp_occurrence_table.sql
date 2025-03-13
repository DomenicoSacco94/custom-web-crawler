CREATE TABLE regexp_occurrences (
    id SERIAL PRIMARY KEY,
    pattern VARCHAR(255) NOT NULL,
    description TEXT,
    surrounding_text TEXT NOT NULL,
    url VARCHAR(255)
);