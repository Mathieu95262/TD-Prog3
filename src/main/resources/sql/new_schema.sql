DROP TABLE IF EXISTS dish_ingredient CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS dish CASCADE;

DROP TYPE IF EXISTS unit_type CASCADE;
DROP TYPE IF EXISTS ingredient_category CASCADE;
DROP TYPE IF EXISTS dish_type CASCADE;

CREATE TYPE dish_type AS ENUM
    ('STARTER', 'MAIN', 'DESSERT');

CREATE TYPE unit_type AS ENUM
    ('PCS', 'KG', 'L');

CREATE TYPE ingredient_category AS ENUM
    ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255),
                      dish_type dish_type,
                      selling_price NUMERIC(10, 2)
);

CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255),
                            price NUMERIC(10, 2),
                            category ingredient_category
);

CREATE TABLE dish_ingredient (
                                 id SERIAL PRIMARY KEY,
                                 dish_id INTEGER REFERENCES dish(id) ON DELETE CASCADE,
                                 ingredient_id INTEGER REFERENCES ingredient(id) ON DELETE RESTRICT,
                                 quantity_required NUMERIC(10, 2) NOT NULL,
                                 unit unit_type NOT NULL
);

ALTER TABLE ingredient ADD COLUMN IF NOT EXISTS stock_quantity NUMERIC(10, 2) DEFAULT 0;

CREATE TABLE IF NOT EXISTS stock_movement (
                                              id SERIAL PRIMARY KEY,
                                              ingredient_id INTEGER REFERENCES ingredient(id) ON DELETE CASCADE,
    quantity NUMERIC(10, 2) NOT NULL,
    type VARCHAR(10) CHECK (type IN ('IN', 'OUT')) NOT NULL,
    unit VARCHAR(10) NOT NULL,
    creation_datetime TIMESTAMP WITH TIME ZONE DEFAULT now()
    );

UPDATE ingredient SET stock_quantity = 5.0 WHERE id = 1;
UPDATE ingredient SET stock_quantity = 4.0 WHERE id = 2;
UPDATE ingredient SET stock_quantity = 10.0 WHERE id = 3;
UPDATE ingredient SET stock_quantity = 3.0 WHERE id = 4;
UPDATE ingredient SET stock_quantity = 2.5 WHERE id = 5;


DROP TYPE IF EXISTS payment_status CASCADE;
CREATE TYPE payment_status AS ENUM ('UNPAID', 'PAID');


CREATE TABLE IF NOT EXISTS "order" (
                                       id SERIAL PRIMARY KEY,
                                       reference VARCHAR(100) UNIQUE NOT NULL,
    creation_datetime TIMESTAMP WITH TIME ZONE DEFAULT now(),
    payment_status payment_status DEFAULT 'UNPAID'
    );


CREATE TABLE IF NOT EXISTS sale (
                                    id SERIAL PRIMARY KEY,
                                    order_id INTEGER UNIQUE REFERENCES "order"(id) ON DELETE CASCADE,
    sale_datetime TIMESTAMP WITH TIME ZONE DEFAULT now()
    );