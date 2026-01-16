package org.example;

import org.example.model.Dish;
import org.example.model.Ingredient;
import org.example.repo.DataRetriever;
import org.example.enums.CategoryEnum;
import org.example.enums.DishTypeEnum;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        System.out.println("=== TEST 1 : Récupération d'un plat existant ===");
        try {
            Dish existingDish = dr.findDishById(1);
            System.out.println("Plat : " + existingDish.getName());
            System.out.println("Ingrédients :");
            for (Ingredient i : existingDish.getIngredients()) {
                System.out.println("  - " + i.getName() + " : " + i.getRequiredQuantity() + " unités, prix = " + i.getPrice());
            }

            System.out.println("Coût total : " + existingDish.getDishCost());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== TEST 2 : Création d'un nouveau plat ===");
        try {
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        System.out.println("\n=== TEST 3 : Plat avec ingrédient sans quantité ===");
        try {
            Ingredient chocolat = new Ingredient("Chocolat", 3.0, CategoryEnum.DAIRY, null);

            Dish dessert = new Dish(null, "Dessert Mystère", DishTypeEnum.DESSERT);
            dessert.setIngredients(List.of(chocolat));

            dr.saveDish(dessert);

            System.out.println("Coût total : " + dessert.getDishCost());

        } catch (Exception e) {
            System.out.println("Erreur attendue : " + e.getMessage());
        }

        System.out.println("\n=== TEST 4 : Mise à jour d'un plat existant ===");
        try {
            Dish dishToUpdate = dr.findDishById(1);
            System.out.println("Plat avant update : " + dishToUpdate.getName());

            for (Ingredient i : dishToUpdate.getIngredients()) {
                if (i.getName().equals("Laitue")) {
                    i.setRequiredQuantity(2.0);
                    i.setPrice(1.5);
                }
            }

            Dish updatedDish = dr.saveDish(dishToUpdate);
            System.out.println("Plat après update : " + updatedDish.getName());
            System.out.println("Nouveau coût total : " + updatedDish.getDishCost());

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
