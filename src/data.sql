INSERT INTO dish (name, dish_type) VALUES
           ('Salade Fraîche', 'START'),
           ('Gâteau au chocolat', 'DESSERT');

INSERT INTO ingredient (name, price, category, id_dish) VALUES
              ('Laitue', 500, 'VEGETABLE', 1),
              ('Tomate', 400, 'VEGETABLE', 1),
              ('Poulet', 3000, 'ANIMAL', NULL),
              ('Chocolat', 2000, 'OTHER', 2);
