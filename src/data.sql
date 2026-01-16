-- Insérer les ingrédients
INSERT INTO ingredients (name, price, category) VALUES
                                                    ('Laitue', 500, 'VEGETABLE'),
                                                    ('Tomate', 400, 'VEGETABLE'),
                                                    ('Poulet', 3000, 'ANIMAL'),
                                                    ('Chocolat', 2000, 'OTHER');

-- Insérer un plat (Salade simple)
INSERT INTO dishes (name, dish_type) VALUES ('Salade simple', 'ENTREE');

-- Associer les ingrédients au plat
INSERT INTO recipe_ingredients (id_dish, id_ingredient, quantity_needed)
SELECT d.id, i.id,
       CASE i.name
           WHEN 'Laitue' THEN 1
           WHEN 'Tomate' THEN 2
           WHEN 'Poulet' THEN 0.5
           END
FROM dishes d, ingredients i
WHERE d.name = 'Salade simple'
  AND i.name IN ('Laitue', 'Tomate', 'Poulet');