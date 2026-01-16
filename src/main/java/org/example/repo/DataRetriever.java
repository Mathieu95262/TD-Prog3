package org.example.repo;

import org.example.config.DBConnection;
import org.example.enums.CategoryEnum;
import org.example.enums.DishTypeEnum;
import org.example.model.Dish;
import org.example.model.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String sql = """
            SELECT d.id AS d_id, d.name AS d_name, d.dish_type,
                   i.id AS i_id, i.name AS i_name, i.price, i.category, i.required_quantity
            FROM dish d
            LEFT JOIN ingredient i ON d.id = i.id_dish
            WHERE d.id = ?
        """;

        try (Connection c = DBConnection.getDBConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            Dish dish = null;
            List<Ingredient> ingredients = new ArrayList<>();

            while (rs.next()) {
                if (dish == null) {
                    dish = new Dish(
                            rs.getInt("d_id"),
                            rs.getString("d_name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type"))
                    );
                }


                if (rs.getObject("i_id") != null) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("i_id"));
                    ingredient.setName(rs.getString("i_name"));
                    ingredient.setPrice(rs.getDouble("price"));
                    ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));


                    Double qty = rs.getObject("required_quantity") != null ?
                            rs.getDouble("required_quantity") : null;
                    ingredient.setRequiredQuantity(qty);

                    ingredients.add(ingredient);
                }
            }

            if (dish == null) throw new RuntimeException("Dish not found");

            dish.setIngredients(ingredients);
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du plat : " + e.getMessage(), e);
        }
    }

    public Dish saveDish(Dish dish) {
        try (Connection c = DBConnection.getDBConnection()) {
            c.setAutoCommit(false);


            if (dish.getId() == null) {
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO dish(name, dish_type) VALUES (?, ?::dish_type_enum) RETURNING id"
                );
                ps.setString(1, dish.getName());
                ps.setString(2, dish.getDishType().name());
                ResultSet rs = ps.executeQuery();
                rs.next();
                dish.setId(rs.getInt(1));
            } else {
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE dish SET name=?, dish_type=? WHERE id=?"
                );
                ps.setString(1, dish.getName());
                ps.setString(2, dish.getDishType().name());
                ps.setInt(3, dish.getId());
                ps.executeUpdate();

                PreparedStatement clear = c.prepareStatement(
                        "UPDATE ingredient SET id_dish=NULL WHERE id_dish=?"
                );
                clear.setInt(1, dish.getId());
                clear.executeUpdate();
            }

            for (Ingredient i : dish.getIngredients()) {
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE ingredient SET id_dish=?, required_quantity=? WHERE name=?"
                );
                ps.setInt(1, dish.getId());

                if (i.getRequiredQuantity() != null) {
                    ps.setDouble(2, i.getRequiredQuantity());
                } else {
                    ps.setNull(2, Types.NUMERIC);
                }

                ps.setString(3, i.getName());
                int updated = ps.executeUpdate();

                if (updated == 0) {
                    PreparedStatement insert = c.prepareStatement(
                            "INSERT INTO ingredient(name, price, category, id_dish, required_quantity) VALUES (?, ?, ?, ?, ?)"
                    );
                    insert.setString(1, i.getName());
                    insert.setDouble(2, i.getPrice());
                    insert.setString(3, i.getCategory().name());
                    insert.setInt(4, dish.getId());
                    if (i.getRequiredQuantity() != null) {
                        insert.setDouble(5, i.getRequiredQuantity());
                    } else {
                        insert.setNull(5, Types.NUMERIC);
                    }
                    insert.executeUpdate();
                }
            }

            c.commit();
            return findDishById(dish.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du plat : " + e.getMessage(), e);
        }
    }

    public List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT * FROM ingredient ORDER BY id LIMIT ? OFFSET ?";

        try (Connection c = DBConnection.getDBConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ingredient i = new Ingredient();
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                i.setPrice(rs.getDouble("price"));
                i.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                Double qty = rs.getObject("required_quantity") != null ? rs.getDouble("required_quantity") : null;
                i.setRequiredQuantity(qty);
                ingredients.add(i);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ingrédients : " + e.getMessage(), e);
        }

        return ingredients;
    }
}
