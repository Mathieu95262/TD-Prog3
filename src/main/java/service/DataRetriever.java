package service;

import org.example.db.DBConnection;
import org.example.model.Product;
import org.example.model.Category;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private final DBConnection db;

    public DataRetriever(DBConnection db) {
        this.db = db;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM product_category";

        try (Connection conn = db.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("product_id")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Pagination
    public List<Product> getProductList(int page, int size) {
        List<Product> list = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT * FROM product ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = db.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getTimestamp("creation_datetime").toInstant(),
                            null
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Filtres
    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax
    ) {
        List<Product> list = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        String sql = """
            SELECT p.*
            FROM product p
            LEFT JOIN product_category c ON p.id = c.product_id
            WHERE 1=1
        """;

        if (productName != null) {
            sql += " AND p.name ILIKE ?";
            params.add("%" + productName + "%");
        }

        if (categoryName != null) {
            sql += " AND c.name ILIKE ?";
            params.add("%" + categoryName + "%");
        }

        if (creationMin != null) {
            sql += " AND p.creation_datetime >= ?";
            params.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            sql += " AND p.creation_datetime <= ?";
            params.add(Timestamp.from(creationMax));
        }

        try (Connection conn = db.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("creation_datetime").toInstant(),
                        null
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Filtres + pagination
    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax,
            int page,
            int size
    ) {
        List<Product> filtered = getProductsByCriteria(
                productName, categoryName, creationMin, creationMax
        );

        int start = (page - 1) * size;
        int end = Math.min(start + size, filtered.size());

        if (start >= filtered.size()) return new ArrayList<>();

        return filtered.subList(start, end);
    }
}
