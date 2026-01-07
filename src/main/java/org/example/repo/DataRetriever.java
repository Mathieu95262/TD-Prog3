package org.example.repo;

import org.example.config.DBConnection;
import org.example.enums.*;
import org.example.model.*;

import java.sql.*;
import java.util.*;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        String sql = """
            SELECT d.id d_id, d.name d_name, d.dish_type,
                   i.id i_id, i.name i_name, i.price, i.category
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

                if (rs.getInt("i_id") != 0) {
                    Ingredient i = new Ingredient();
                    i.setId(rs.getInt("i_id"));
                    i.setName(rs.getString("i_name"));
                    i.setPrice(rs.getDouble("price"));
                    i.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ingredients.add(i);
                }
            }

            if (dish == null) throw new RuntimeException("Dish not found");

            dish.setIngredients(ingredients);
            return dish;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findIngredients(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM ingredient ORDER BY id LIMIT ? OFFSET ?";

        List<Ingredient> list = new ArrayList<>();

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
                list.add(i);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> createIngredients(List<Ingredient> ingredients) {
        String checkSql = "SELECT COUNT(*) FROM ingredient WHERE name = ?";
        String insertSql = "INSERT INTO ingredient(name, price, category) VALUES (?, ?, ?)";

        try (Connection c = DBConnection.getDBConnection()) {
            c.setAutoCommit(false);

            for (Ingredient i : ingredients) {
                PreparedStatement check = c.prepareStatement(checkSql);
                check.setString(1, i.getName());
                ResultSet rs = check.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    throw new RuntimeException("Ingredient already exists");
                }
            }

            for (Ingredient i : ingredients) {
                PreparedStatement ps = c.prepareStatement(insertSql);
                ps.setString(1, i.getName());
                ps.setDouble(2, i.getPrice());
                ps.setString(3, i.getCategory().name());
                ps.executeUpdate();
            }

            c.commit();
            return ingredients;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Dish saveDish(Dish dish) {
        try (Connection c = DBConnection.getDBConnection()) {
            c.setAutoCommit(false);

            if (dish.getId() == null) {
                PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO dish(name, dish_type) VALUES (?, ?) RETURNING id");
                ps.setString(1, dish.getName());
                ps.setString(2, dish.getDishType().name());
                ResultSet rs = ps.executeQuery();
                rs.next();
                dish.setId(rs.getInt(1));
            } else {
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE dish SET name=?, dish_type=? WHERE id=?");
                ps.setString(1, dish.getName());
                ps.setString(2, dish.getDishType().name());
                ps.setInt(3, dish.getId());
                ps.executeUpdate();

                PreparedStatement clear = c.prepareStatement(
                        "UPDATE ingredient SET id_dish=NULL WHERE id_dish=?");
                clear.setInt(1, dish.getId());
                clear.executeUpdate();
            }

            for (Ingredient i : dish.getIngredients()) {
                PreparedStatement ps = c.prepareStatement(
                        "UPDATE ingredient SET id_dish=? WHERE name=?");
                ps.setInt(1, dish.getId());
                ps.setString(2, i.getName());
                ps.executeUpdate();
            }

            c.commit();
            return dish;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

