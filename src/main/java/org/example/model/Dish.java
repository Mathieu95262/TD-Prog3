package org.example.model;

import java.util.List;

class UnknownQuantityException extends RuntimeException {
    public UnknownQuantityException(String message) {
        super(message);
    }
}

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    public Dish(Integer id, String saladeSpéciale, org.example.enums.DishTypeEnum start) {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DishTypeEnum getDishType() { return dishType; }
    public void setDishType(DishTypeEnum dishType) { this.dishType = dishType; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public Double getDishCost() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }

        double totalCost = 0.0;

        for (Ingredient ingredient : ingredients) {
            Double qty = ingredient.getRequiredQuantity();
            if (qty == null) {
                throw new UnknownQuantityException(
                        "Quantité nécessaire inconnue pour l'ingrédient : " + ingredient.getName()
                );
            }
            totalCost += ingredient.getPrice() * qty;
        }

        return totalCost;
    }
    public enum DishTypeEnum {
        START,
        MAIN_COURSE,
        DESSERT;

        @Override
        public String toString() {
            switch (this) {
                case START:
                    return "Start";
                case MAIN_COURSE:
                    return "Main Course";
                case DESSERT:
                    return "Dessert";
                default:
                    return super.toString();
            }
        }
    }
}
