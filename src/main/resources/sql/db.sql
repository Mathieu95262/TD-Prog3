DROP DATABASE IF EXISTS mini_dish_db;
DROP USER IF EXISTS mini_dish_db_manager;
CREATE DATABASE mini_dish_db;

\c mini_dish_db;

CREATE USER mini_dish_db_manager WITH PASSWORD '123456';

GRANT ALL PRIVILEGES ON DATABASE mini_dish_db TO mini_dish_db_manager;
GRANT CREATE ON SCHEMA public TO mini_dish_db_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA Public
   GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO mini_dish_db_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA Public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO mini_dish_db_manager;