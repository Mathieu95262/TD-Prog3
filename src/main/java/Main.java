import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        Dish salade = dr.findDishById(1);
        System.out.println("\nSalade fraîche (ID 1):");
        System.out.println("  - Nom: " + salade.getName());
        System.out.println("  - Type: " + salade.getDishType());
        System.out.println("  - Prix vente: " + salade.getSellingPrice() + " Ar");
        System.out.println("  - Nombre ingrédients: " + salade.getDishIngredients().size());
        System.out.println("  - Coût: " + salade.getDishCost() + " Ar (attendu: 250)");
        System.out.println("  - Marge: " + salade.getGrossMargin() + " Ar (attendu: 3250)");

        Dish poulet = dr.findDishById(2);
        System.out.println("\nPoulet grillé (ID 2):");
        System.out.println("  - Nom: " + poulet.getName());
        System.out.println("  - Coût: " + poulet.getDishCost() + " Ar (attendu: 4500)");
        System.out.println("  - Marge: " + poulet.getGrossMargin() + " Ar (attendu: 7500)");

        Dish gateau = dr.findDishById(4);
        System.out.println("\nGâteau au chocolat (ID 4):");
        System.out.println("  - Nom: " + gateau.getName());
        System.out.println("  - Coût: " + gateau.getDishCost() + " Ar (attendu: 1400)");
        System.out.println("  - Marge: " + gateau.getGrossMargin() + " Ar (attendu: 6600)");

        System.out.println("\n2.PLAT SANS PRIX :");
        try {
            Dish riz = dr.findDishById(3);
            System.out.println("Riz aux légumes: " + riz.getName());
            System.out.println("Prix vente: " + riz.getSellingPrice());
            double marge = riz.getGrossMargin();
            System.out.println("ERREUR: Pas d'exception!");
        } catch (RuntimeException e) {
            System.out.println("Exception correcte: " + e.getMessage());
        }

        System.out.println("\n3.SAUVEGARDE DE PLAT :");
        try {
            Dish nouveauPlat = new Dish();
            nouveauPlat.setName("Test Plat");
            nouveauPlat.setDishType(DishTypeEnum.MAIN);
            nouveauPlat.setSellingPrice(5000.0);

            Dish platSauvegarde = dr.saveDish(nouveauPlat);
            System.out.println("Plat sauvegardé: ID " + platSauvegarde.getId() + " - " + platSauvegarde.getName());
        } catch (Exception e) {
            System.out.println("Erreur sauvegarde: " + e.getMessage());
        }

        System.out.println("\n4. TEST STOCK INITIAL DES INGRÉDIENTS");
        System.out.println("Stock initial ");
        System.out.println("  - Laitue: 5.0 KG (attendu)");
        System.out.println("  - Tomate: 4.0 KG (attendu)");
        System.out.println("  - Poulet: 10.0 KG (attendu)");
        System.out.println("  - Chocolat: 3.0 KG (attendu)");
        System.out.println("  - Beurre: 2.5 KG (attendu)");

        System.out.println("\n5. TEST getStockValueAt()");
        Instant testTime = LocalDateTime.of(2024, 1, 6, 12, 0).toInstant(ZoneOffset.UTC);
        System.out.println("Stock au 2024-01-06 12:00 (après mouvements OUT):");

        double[] stocksAttendus = {4.8, 3.85, 9.0, 2.7, 2.3};
        String[] nomsIngredients = {"Laitue", "Tomate", "Poulet", "Chocolat", "Beurre"};

        for (int i = 1; i <= 5; i++) {
            try {
                Double stockValue = dr.getStockValueAt(i, testTime);
                double stockAttendu = stocksAttendus[i-1];
                boolean correct = Math.abs(stockValue - stockAttendu) < 0.01;

                System.out.printf("  %s: %.2f KG (attendu: %.2f KG) %s%n",
                        nomsIngredients[i-1],
                        stockValue,
                        stockAttendu,
                        correct ? "✓" : "✗ ERREUR");

                if (!correct) {
                    System.out.printf("    Différence: %.2f KG%n", stockValue - stockAttendu);
                }
            } catch (Exception e) {
                System.out.println("  Erreur pour " + nomsIngredients[i-1] + ": " + e.getMessage());
            }
        }

        System.out.println("\n6. TEST saveIngredient() avec nouveaux mouvements:");
        try {
            Ingredient laitue = dr.findIngredientById(1);
            System.out.println("Avant sauvegarde:");
            System.out.println("  - Nom: " + laitue.getName());
            System.out.println("  - Stock actuel: " + laitue.getStockQuantity() + " KG");
            System.out.println("  - Nombre de mouvements: " +
                    (laitue.getStockMovementList() != null ? laitue.getStockMovementList().size() : 0));

            StockMovement nouveauMouvement = new StockMovement();
            nouveauMouvement.setQuantity(2.0);
            nouveauMouvement.setType(StockMovementType.IN);
            nouveauMouvement.setUnit(UnitEnum.KG);
            nouveauMouvement.setCreationDatetime(Instant.now());

            if (laitue.getStockMovementList() == null) {
                laitue.setStockMovementList(new ArrayList<>());
            }
            laitue.getStockMovementList().add(nouveauMouvement);

            laitue.setStockQuantity(laitue.getStockQuantity() + 2.0);

            Ingredient updated = dr.saveIngredient(laitue);
            System.out.println("\nAprès sauvegarde avec mouvement IN de 2.0 KG:");
            System.out.println("  - Stock mis à jour: " + updated.getStockQuantity() + " KG");
            System.out.println("  - Nombre de mouvements total: " +
                    (updated.getStockMovementList() != null ? updated.getStockMovementList().size() : 0));

            Double stockApresMouvement = dr.getStockValueAt(1, Instant.now());
            System.out.println("  - Stock vérifié via getStockValueAt(): " + stockApresMouvement + " KG");
        } catch (Exception e) {
            System.out.println("Erreur lors du test saveIngredient: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n7. TEST findIngredientById() avec mouvements:");
        try {
            System.out.println("Vérification du chargement complet des ingrédients:");
            for (int i = 1; i <= 3; i++) {
                Ingredient ing = dr.findIngredientById(i);
                System.out.printf("  %s (ID %d):%n", ing.getName(), ing.getId());
                System.out.printf("    - Stock: %.2f KG%n", ing.getStockQuantity());
                System.out.printf("    - Mouvements chargés: %d%n",
                        ing.getStockMovementList() != null ? ing.getStockMovementList().size() : 0);
            }
        } catch (Exception e) {
            System.out.println("Erreur findIngredientById: " + e.getMessage());
        }

        System.out.println("\nCONVERSIONS D'UNITÉS");

        System.out.println("\nCalcul des sorties selon les conversions :");
        String[] ingredients = {"Tomate", "Laitue", "Chocolat", "Poulet", "Beurre"};
        double[] quantities = {5, 2, 1, 4, 1};
        UnitEnum[] units = {UnitEnum.PCS, UnitEnum.PCS, UnitEnum.L, UnitEnum.PCS, UnitEnum.L};
        double[] stocksAvant = {4.0, 5.0, 3.0, 10.0, 2.5};

        for (int i = 0; i < ingredients.length; i++) {
            double stockFinal = stocksAvant[i] - Unit.convertToKG(ingredients[i], quantities[i], units[i]);

            System.out.printf("\n%s:%n", ingredients[i]);
            System.out.printf("  - Sortie: %.0f %s%n", quantities[i], units[i]);
            System.out.printf("  - Conversion: %.0f %s = %.1f KG%n", quantities[i], units[i],
                    Unit.convertToKG(ingredients[i], quantities[i], units[i]));
            System.out.printf("  - Stock avant: %.1f KG%n", stocksAvant[i]);
            System.out.printf("  - Stock final: %.1f KG%n", stockFinal);
        }

        System.out.println("\n\nRésumé des stocks finaux attendus :");
        System.out.println("  - Laitue: 4.0 KG (5.0 - 1.0)");
        System.out.println("  - Tomate: 3.5 KG (4.0 - 0.5)");
        System.out.println("  - Poulet: 9.5 KG (10.0 - 0.5)");
        System.out.println("  - Chocolat: 2.6 KG (3.0 - 0.4)");
        System.out.println("  - Beurre: 2.3 KG (2.5 - 0.2)");

        System.out.println("\nTEST AVEC LA BASE DE DONNÉES");
        try {
            Ingredient tomate = dr.findIngredientById(2);
            System.out.println("\nTomate avant mouvement:");
            System.out.println("  - Stock: " + tomate.getStockQuantity() + " KG");

            StockMovement mouvementTomate = new StockMovement();
            mouvementTomate.setQuantity(5.0);
            mouvementTomate.setType(StockMovementType.OUT);
            mouvementTomate.setUnit(UnitEnum.PCS);
            mouvementTomate.setCreationDatetime(Instant.now());

            if (tomate.getStockMovementList() == null) {
                tomate.setStockMovementList(new ArrayList<>());
            }
            tomate.getStockMovementList().add(mouvementTomate);

            double conversionTomate = Unit.convertToKG("Tomate", 5.0, UnitEnum.PCS);
            tomate.setStockQuantity(tomate.getStockQuantity() - conversionTomate);

            Ingredient tomateUpdated = dr.saveIngredient(tomate);
            System.out.println("\nAprès mouvement OUT de 5 PCS de Tomate:");
            System.out.println("  - Stock mis à jour: " + tomateUpdated.getStockQuantity() + " KG");
            System.out.println("  - Conversion appliquée: 5 PCS = " + conversionTomate + " KG");

            Double stockVerifie = dr.getStockValueAt(2, Instant.now());
            System.out.println("  - Vérification via getStockValueAt(): " + stockVerifie + " KG");
        } catch (Exception e) {
            System.out.println("Erreur lors du test pratique: " + e.getMessage());
        }

    }
}