CREATE TABLE IF NOT EXISTS person (
   id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
   version INT NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   name VARCHAR(70) NOT NULL,
   email VARCHAR(254),
   phone_number VARCHAR(50)
);