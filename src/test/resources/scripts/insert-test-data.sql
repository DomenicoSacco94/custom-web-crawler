-- Insert a topic for "Bezirksstelle Lüneburg"
INSERT INTO topics (id,name, description) VALUES (1,'Bezirksstelle Lüneburg', 'Topic for Bezirksstelle Lüneburg');

-- Insert a case-insensitive regexp associated with the topic
INSERT INTO regexps (topic_id, pattern, description)
VALUES (
           (SELECT id FROM topics WHERE name = 'Bezirksstelle Lüneburg'),
           '(?i)Bezirksstelle\sLüneburg',
           'Lüneburg District Office'
       );