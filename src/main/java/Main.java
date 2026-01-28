public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();
        System.out.println("1. TEST DES PLATS :");
        // Salade
        Dish salade = dr.findDishById(1);
        System.out.println("\nSalade fraîche (ID 1):");
        System.out.println("  - Nom: " + salade.getName());
        System.out.println("  - Type: " + salade.getDishType());
        System.out.println("  - Prix vente: " + salade.getSellingPrice() + " Ar");
        System.out.println("  - Nombre ingrédients: " + salade.getDishIngredients().size());
        System.out.println("  - Coût: " + salade.getDishCost() + " Ar (attendu: 250)");
        System.out.println("  - Marge: " + salade.getGrossMargin() + " Ar (attendu: 3250)");

        // Poulet
        Dish poulet = dr.findDishById(2);
        System.out.println("\nPoulet grillé (ID 2):");
        System.out.println("  - Nom: " + poulet.getName());
        System.out.println("  - Coût: " + poulet.getDishCost() + " Ar (attendu: 4500)");
        System.out.println("  - Marge: " + poulet.getGrossMargin() + " Ar (attendu: 7500)");

        // Gateau
        Dish gateau = dr.findDishById(4);
        System.out.println("\nGâteau au chocolat (ID 4):");
        System.out.println("  - Nom: " + gateau.getName());
        System.out.println("  - Coût: " + gateau.getDishCost() + " Ar (attendu: 1400)");
        System.out.println("  - Marge: " + gateau.getGrossMargin() + " Ar (attendu: 6600)");

        System.out.println("\n2. TEST PLAT SANS PRIX :");
        try {
            Dish riz = dr.findDishById(3);
            System.out.println("Riz aux légumes: " + riz.getName());
            System.out.println("Prix vente: " + riz.getSellingPrice());
            double marge = riz.getGrossMargin();
            System.out.println("ERREUR: Pas d'exception!");
        } catch (RuntimeException e) {
            System.out.println("Exception correcte: " + e.getMessage());
        }
        System.out.println("\n3. TEST SAUVEGARDE PLAT :");
        try {
            Dish nouveauPlat = new Dish();
            nouveauPlat.setName("Test Plat");
            nouveauPlat.setDishType(DishTypeEnum.MAIN);
            nouveauPlat.setSellingPrice(5000.0);

            Dish platSauvegarde = dr.saveDish(nouveauPlat);
            System.out.println("Plat sauvegardé: ID " + platSauvegarde.getId() +
                    " - " + platSauvegarde.getName());

        } catch (Exception e) {
            System.out.println("Erreur sauvegarde: " + e.getMessage());
        }
    }
}