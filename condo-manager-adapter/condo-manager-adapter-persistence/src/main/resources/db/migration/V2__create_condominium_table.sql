CREATE TABLE IF NOT EXISTS condominium (
   id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
   version INT NOT NULL DEFAULT 0,
   created_at TIMESTAMP NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
   created_by VARCHAR(254),
   updated_at TIMESTAMP NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
   updated_by VARCHAR(254),
   city VARCHAR(50) NOT NULL,
   street VARCHAR(200) NOT NULL,
   house_number INT NOT NULL,
   lat NUMERIC,
   lon NUMERIC
);