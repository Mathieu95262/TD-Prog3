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