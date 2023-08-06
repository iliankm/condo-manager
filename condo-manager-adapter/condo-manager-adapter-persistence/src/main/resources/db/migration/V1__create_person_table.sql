CREATE TABLE IF NOT EXISTS person (
   id VARCHAR(36) PRIMARY KEY,
   version INT NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   name VARCHAR(70) NOT NULL,
   email VARCHAR(254),
   phone_number VARCHAR(50)
);