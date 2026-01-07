-- ENUMS
CREATE TYPE category_enum AS ENUM (
    'VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER'
);

CREATE TYPE dish_type_enum AS ENUM (
    'START', 'MAIN', 'DESSERT'
);

CREATE TABLE Dish(
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    dish_type VARCHAR(100) NOT NULL,
)
CREATET TABLE Ingredient(
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price NUMBER(10,2) NOT NULL,
    category VARCHAR(150)  NOT NULL,
    id_dish INTEGER NOT NULL,

    CONSTRAINT fk_ingredient_dish
    FOREIGN KEY (id_dish)
    REFERENCES Dish(id)
    ON DELETE CASCADE
);