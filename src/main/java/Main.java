public class Main {
    public static void main(String[] args) throws Exception {
        DataRetriever dr = new DataRetriever();

        // Test 1 : récupérer un plat
        Dish salade = dr.findDishById(1);
        System.out.println("Plat trouvé : " + salade);

        // Test 2 : calculer le coût
        System.out.println("Coût Salade fraîche : " + salade.getDishCost());

        // Test 3 : calculer la marge brute
        System.out.println("Marge brute Salade fraîche : " + salade.getGrossMargin());

        // Tu peux tester aussi les autres plats
        Dish poulet = dr.findDishById(2);
        System.out.println("Coût Poulet grillé : " + poulet.getDishCost());
        System.out.println("Marge brute Poulet grillé : " + poulet.getGrossMargin());

        Dish gateau = dr.findDishById(4);
        System.out.println("Coût Gâteau au chocolat : " + gateau.getDishCost());
        System.out.println("Marge brute Gâteau au chocolat : " + gateau.getGrossMargin());
    }
}
