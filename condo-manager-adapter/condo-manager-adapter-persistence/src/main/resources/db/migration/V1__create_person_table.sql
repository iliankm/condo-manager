CREATE TABLE IF NOT EXISTS person (
   id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
   version INT NOT NULL DEFAULT 0,
   created_at TIMESTAMP NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
   created_by VARCHAR(254),
   updated_at TIMESTAMP NOT NULL DEFAULT (NOW() AT TIME ZONE 'UTC'),
   updated_by VARCHAR(254),
   name VARCHAR(70) NOT NULL,
   email VARCHAR(254),
   phone_number VARCHAR(50)
);