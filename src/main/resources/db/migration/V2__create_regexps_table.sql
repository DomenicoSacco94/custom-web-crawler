CREATE TABLE regexps (
                                   id SERIAL PRIMARY KEY,
                                   topic_id BIGINT NOT NULL,
                                   pattern TEXT NOT NULL,
                                   description TEXT,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_topic
                                       FOREIGN KEY(topic_id)
                                       REFERENCES topics(id)
);