CREATE TABLE regexps (
                                   id SERIAL PRIMARY KEY,
                                   pattern TEXT NOT NULL,
                                   description TEXT,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);