-- Création du type enum
CREATE TYPE dish_type_enum AS ENUM ('ENTREE', 'PLAT_PRINCIPAL', 'DESSERT', 'BOISSON');

-- Table des plats
CREATE TABLE dishes (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        dish_type dish_type_enum NOT NULL
);

-- Table des ingrédients (prix unitaire)
CREATE TABLE ingredients (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             category VARCHAR(50)
);

-- Table de liaison entre plats et ingrédients
CREATE TABLE recipe_ingredients (
                                    id_dish INTEGER REFERENCES dishes(id) ON DELETE CASCADE,
                                    id_ingredient INTEGER REFERENCES ingredients(id) ON DELETE CASCADE,
                                    quantity_needed DECIMAL(10,2) NOT NULL,
                                    PRIMARY KEY (id_dish, id_ingredient)
);