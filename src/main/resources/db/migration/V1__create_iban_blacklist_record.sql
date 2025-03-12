CREATE TABLE blacklisted_ibans (
                                   id SERIAL PRIMARY KEY,
                                   iban VARCHAR(34) UNIQUE NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);