create type dish_type as enum ('STARTER', 'MAIN', 'DESSERT');


create table dish
(
    id        serial primary key,
    name      varchar(255),
    dish_type dish_type
);

create type ingredient_category as enum ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

create table ingredient
(
    id       serial primary key,
    name     varchar(255),
    price    numeric(10, 2),
    category ingredient_category,
    id_dish  int references dish (id)
);
ALTER TABLE ingredient DROP COLUMN IF EXISTS id_dish;
ALTER TABLE dish ADD COLUMN IF NOT EXISTS selling_price NUMERIC(10,2);

alter table dish
    add column if not exists price numeric(10, 2);


alter table ingredient
    add column if not exists required_quantity numeric(10, 2);

    CREATE TABLE IF NOT EXISTS category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dish_type (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dish (
  id SERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  category_id INTEGER REFERENCES category(id) ON DELETE SET NULL,
  dish_type_id INTEGER REFERENCES dish_type(id) ON DELETE SET NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  UNIQUE (name)
);
CREATE TABLE IF NOT EXISTS ingredient (
  id SERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL UNIQUE,
  unit_default VARCHAR(50) 
);

CREATE TABLE IF NOT EXISTS dish_ingredient (
  dish_id INTEGER NOT NULL REFERENCES dish(id) ON DELETE CASCADE,
  ingredient_id INTEGER NOT NULL REFERENCES ingredient(id) ON DELETE RESTRICT,
  qty NUMERIC(10,3) NOT NULL DEFAULT 0,
  unit VARCHAR(50)
);

CREATE INDEX IF NOT EXISTS idx_dish_category ON dish(category_id);
CREATE INDEX IF NOT EXISTS idx_dish_type ON dish(dish_type_id);
CREATE INDEX IF NOT EXISTS idx_ingredient_name ON ingredient(name);
