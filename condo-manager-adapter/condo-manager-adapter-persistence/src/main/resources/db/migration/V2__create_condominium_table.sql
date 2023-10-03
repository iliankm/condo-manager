CREATE TABLE IF NOT EXISTS condominium (
   id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
   version INT NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   city VARCHAR(50) NOT NULL,
   street VARCHAR(200) NOT NULL,
   house_number INT NOT NULL,
   lat NUMERIC,
   lon NUMERIC
);