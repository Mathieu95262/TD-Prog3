package org.example;
import org.example.db.DBConnection;
import service.DataRetriever;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(dbConnection);
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("=== CATEGORIES ===");
        System.out.println(dataRetriever.getAllCategories());

        System.out.println("=== PAGINATION ===");
        System.out.println(dataRetriever.getProductList(1, 2));

        System.out.println("=== FILTRE ===");
        System.out.println(dataRetriever.getProductsByCriteria("phone", null, null, null));

        System.out.println("=== FILTRE + PAGINATION ===");
        System.out.println(dataRetriever.getProductsByCriteria("laptop", null, null, null, 1, 1));
    }
}