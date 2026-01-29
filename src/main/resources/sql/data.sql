INSERT INTO ingredient (id, name, price, category) VALUES
                                                       (1, 'Laitue', 800.0, 'VEGETABLE'),
                                                       (2, 'Tomate', 600.0, 'VEGETABLE'),
                                                       (3, 'Poulet', 4500.0, 'ANIMAL'),
                                                       (4, 'Chocolat', 3000.0, 'OTHER'),
                                                       (5, 'Beurre', 2500.0, 'DAIRY');

INSERT INTO dish (id, name, dish_type, selling_price) VALUES
                                                          (1, 'Salade fraîche', 'STARTER', 3500.00),
                                                          (2, 'Poulet grillé', 'MAIN', 12000.00),
                                                          (3, 'Riz aux légumes', 'MAIN', NULL),
                                                          (4, 'Gâteau au chocolat', 'DESSERT', 8000.00),
                                                          (5, 'Salade de fruits', 'DESSERT', NULL);

INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity_required, unit) VALUES
                                                                                  (1, 1, 0.20, 'KG'),
                                                                                  (1, 2, 0.15, 'KG'),
                                                                                  (2, 3, 1.00, 'KG'),
                                                                                  (4, 4, 0.30, 'KG'),
                                                                                  (4, 5, 0.20, 'KG');
INSERT INTO stock_movement (id, ingredient_id, quantity, type, unit, creation_datetime) VALUES
                                                                                            (6, 1, 0.2, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (7, 2, 0.15, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (8, 3, 1.0, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (9, 4, 0.3, 'OUT', 'KG', '2024-01-06 12:00:00'),
                                                                                            (10, 5, 0.2, 'OUT', 'KG', '2024-01-06 12:00:00');