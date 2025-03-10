CREATE TABLE food (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    barcode VARCHAR(63) NOT NULL UNIQUE,
    image_url VARCHAR(255),
    calories NUMERIC(6,2) NOT NULL,
    fat NUMERIC(6,2) NOT NULL,
    saturated_fat NUMERIC(6,2),
    carbs NUMERIC(6,2) NOT NULL,
    sugars NUMERIC(6,2),
    fiber NUMERIC(6,2) NOT NULL,
    protein NUMERIC(6,2) NOT NULL,
    salt NUMERIC(6,2),
    serving_size_in_grams NUMERIC(6,2) NOT NULL
);