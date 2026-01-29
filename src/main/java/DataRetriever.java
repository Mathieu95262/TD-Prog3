import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(Integer id) {
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     """
                     SELECT id, name, dish_type, selling_price
                     FROM dish
                     WHERE id = ?;
                     """
             )) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                    dish.setSellingPrice(rs.getObject("selling_price") == null
                            ? null : rs.getDouble("selling_price"));

                    dish.setDishIngredients(findDishIngredientsByDishId(id));
                    return dish;
                }
            }
            throw new RuntimeException("Dish not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DishIngredient> findDishIngredientsByDishId(Integer dishId) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     """
                     SELECT di.id AS di_id,
                            di.quantity_required,
                            di.unit,
                            i.id AS ing_id,
                            i.name,
                            i.price,
                            i.category
                     FROM dish_ingredient di
                     JOIN ingredient i ON di.ingredient_id = i.id
                     WHERE di.dish_id = ?;
                     """
             )) {
            ps.setInt(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient(
                            rs.getInt("ing_id"),
                            rs.getString("name"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getDouble("price")
                    );
                    DishIngredient di = new DishIngredient(
                            rs.getInt("di_id"),
                            null,
                            ing,
                            rs.getDouble("quantity_required"),
                            UnitEnum.valueOf(rs.getString("unit"))
                    );
                    dishIngredients.add(di);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredients;
    }

    public Dish saveDish(Dish toSave) {
        String upsertDishSql = """
            INSERT INTO dish (id, name, dish_type, selling_price)
            VALUES (?, ?, ?::dish_type, ?)
            ON CONFLICT (id) DO UPDATE
            SET name = EXCLUDED.name,
                dish_type = EXCLUDED.dish_type,
                selling_price = EXCLUDED.selling_price
            RETURNING id
            """;

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            Integer dishId;
            try (PreparedStatement ps = conn.prepareStatement(upsertDishSql)) {
                if (toSave.getId() != null) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "dish", "id"));
                }
                ps.setString(2, toSave.getName());
                ps.setString(3, toSave.getDishType().name());
                if (toSave.getSellingPrice() != null) {
                    ps.setDouble(4, toSave.getSellingPrice());
                } else {
                    ps.setNull(4, Types.DOUBLE);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    dishId = rs.getInt(1);
                }
            }

            detachDishIngredients(conn, dishId);
            attachDishIngredients(conn, dishId, toSave.getDishIngredients());

            conn.commit();
            return findDishById(dishId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void detachDishIngredients(Connection conn, Integer dishId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM dish_ingredient WHERE dish_id = ?")) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        }
    }

    private void attachDishIngredients(Connection conn, Integer dishId, List<DishIngredient> dishIngredients)
            throws SQLException {
        if (dishIngredients == null || dishIngredients.isEmpty()) return;

        String sql = """
                INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity_required, unit)
                VALUES (?, ?, ?, ?::unit_type)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DishIngredient di : dishIngredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, di.getIngredient().getId());
                ps.setDouble(3, di.getQuantityRequired());
                ps.setString(4, di.getUnit().name());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private String getSerialSequenceName(Connection conn, String tableName, String columnName)
            throws SQLException {
        String sql = "SELECT pg_get_serial_sequence(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    private int getNextSerialValue(Connection conn, String tableName, String columnName)
            throws SQLException {
        String sequenceName = getSerialSequenceName(conn, tableName, columnName);
        if (sequenceName == null) {
            throw new IllegalArgumentException("No sequence found for " + tableName + "." + columnName);
        }
        updateSequenceNextValue(conn, tableName, columnName, sequenceName);

        String nextValSql = "SELECT nextval(?)";
        try (PreparedStatement ps = conn.prepareStatement(nextValSql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void updateSequenceNextValue(Connection conn, String tableName, String columnName, String sequenceName)
            throws SQLException {
        String setValSql = String.format(
                "SELECT setval('%s', (SELECT COALESCE(MAX(%s), 0) FROM %s))",
                sequenceName, columnName, tableName
        );
        try (PreparedStatement ps = conn.prepareStatement(setValSql);
             ResultSet rs = ps.executeQuery()) {
        }
    }

    public Ingredient findIngredientById(Integer id) {
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, price, category, stock_quantity FROM ingredient WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setStockQuantity(rs.getDouble("stock_quantity"));

                    List<StockMovement> movements = loadStockMovements(conn, id);
                    ing.setStockMovementList(movements);

                    return ing;
                }
            }
            throw new RuntimeException("Ingredient not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<StockMovement> loadStockMovements(Connection conn, Integer ingredientId) throws SQLException {
        List<StockMovement> movements = new ArrayList<>();

        String sql = """
        SELECT id, quantity, type, unit, creation_datetime
        FROM stock_movement
        WHERE ingredient_id = ?
        ORDER BY creation_datetime
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockMovement movement = new StockMovement();
                    movement.setId(rs.getInt("id"));
                    movement.setQuantity(rs.getDouble("quantity"));
                    movement.setType(StockMovementType.valueOf(rs.getString("type")));
                    movement.setUnit(UnitEnum.valueOf(rs.getString("unit")));
                    movement.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
                    movements.add(movement);
                }
            }
        }
        return movements;
    }

    private void saveStockMovements(Connection conn, Integer ingredientId,
                                    List<StockMovement> movements) throws SQLException {
        if (movements == null || movements.isEmpty()) return;

        String sql = """
        INSERT INTO stock_movement (ingredient_id, quantity, type, unit, creation_datetime)
        VALUES (?, ?, ?, ?::unit_type, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (StockMovement movement : movements) {
                if (movement.getId() == null) {
                    ps.setInt(1, ingredientId);
                    ps.setDouble(2, movement.getQuantity());
                    ps.setString(3, movement.getType().name());
                    ps.setString(4, movement.getUnit().name());

                    if (movement.getCreationDatetime() != null) {
                        ps.setTimestamp(5, Timestamp.from(movement.getCreationDatetime()));
                    } else {
                        ps.setTimestamp(5, Timestamp.from(Instant.now()));
                    }
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    public Ingredient saveIngredient(Ingredient toSave) {
        String sql = """
        INSERT INTO ingredient (id, name, price, category, stock_quantity)
        VALUES (?, ?, ?, ?::ingredient_category, ?)
        ON CONFLICT (id) DO UPDATE
        SET name = EXCLUDED.name,
            price = EXCLUDED.price,
            category = EXCLUDED.category,
            stock_quantity = EXCLUDED.stock_quantity
        RETURNING id
        """;

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            Integer ingredientId;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (toSave.getId() != null) {
                    ps.setInt(1, toSave.getId());
                } else {
                    ps.setInt(1, getNextSerialValue(conn, "ingredient", "id"));
                }
                ps.setString(2, toSave.getName());
                ps.setDouble(3, toSave.getPrice());
                ps.setString(4, toSave.getCategory().name());
                if (toSave.getStockQuantity() != null) {
                    ps.setDouble(5, toSave.getStockQuantity());
                } else {
                    ps.setDouble(5, 0.0);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    ingredientId = rs.getInt(1);
                }
            }

            if (toSave.getStockMovementList() != null && !toSave.getStockMovementList().isEmpty()) {
                saveStockMovements(conn, ingredientId, toSave.getStockMovementList());
            }

            conn.commit();
            return findIngredientById(ingredientId);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur saveIngredient: " + e.getMessage(), e);
        }
    }

    public Double getStockValueAt(Integer ingredientId, Instant t) {
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement psStock = conn.prepareStatement("SELECT stock_quantity FROM ingredient WHERE id = ?")) {
            psStock.setInt(1, ingredientId);
            try (ResultSet rs = psStock.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Ingredient not found");
                double stockInitial = rs.getDouble("stock_quantity");

                String movementSql = """
                SELECT
                    SUM(CASE WHEN type = 'IN' THEN quantity ELSE 0 END) as total_in,
                    SUM(CASE WHEN type = 'OUT' THEN quantity ELSE 0 END) as total_out
                FROM stock_movement
                WHERE ingredient_id = ? AND creation_datetime <= ?
                """;

                try (PreparedStatement psMove = conn.prepareStatement(movementSql)) {
                    psMove.setInt(1, ingredientId);
                    psMove.setTimestamp(2, Timestamp.from(t));
                    try (ResultSet rsMove = psMove.executeQuery()) {
                        double totalIn = 0, totalOut = 0;
                        if (rsMove.next()) {
                            totalIn = rsMove.getDouble("total_in");
                            totalOut = rsMove.getDouble("total_out");
                        }
                        return stockInitial + totalIn - totalOut;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}