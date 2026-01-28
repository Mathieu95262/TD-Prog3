CREATE DATABASE product_management_db;

       CREATE USER product_manager_user WITH PASSWORD '123456';

        GRANT CONNECT ON DATABASE product_management_db TO product_manager_user;
        GRANT CREATE ON DATABASE product_management_db TO product_manager_user;
        GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO product_manager_user;
        GRANT ALL PRIVILEGES ON ALL SEQUENCES ON ALL SEQUENCES IN SCHEMA public TO product_manager_user;


INSERT INTO Dish (id, name, dish_type, selling_price) VALUES
                                                          (1, 'Salade fraîche', 'STARTER', 3500.00),
                                                          (2, 'Poulet grillé', 'MAIN', 12000.00),
                                                          (3, 'Riz aux légumes', 'MAIN', NULL),
                                                          (4, 'Gâteau au chocolat', 'DESSERT', 8000.00),
                                                          (5, 'Salade de fruits', 'DESSERT', NULL);

INSERT INTO DishIngredient (id, name, price, category) VALUES
                                                           (1, 'Laitue', 800.0, 'VEGETABLE'),
                                                           (2, 'Tomate', 600.0, 'VEGETABLE'),
                                                           (3, 'Poulet', 4500.0, 'ANIMAL'),
                                                           (4, 'Chocolat', 3000.0, 'OTHER'),
                                                           (5, 'Beurre', 2500.0, 'DAIRY');

INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity_required, unit) VALUES
                                                                                  (1, 1, 0.20, 'KG'),
                                                                                  (1, 2, 0.15, 'KG'),
                                                                                  (2, 3, 1.00, 'KG'),
                                                                                  (4, 4, 0.30, 'KG'),
                                                                                  (4, 5, 0.20, 'KG');