import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;
    private List<DishIngredient> dishIngredients;

    public Dish() {}

    public Dish(Integer id, String name, DishTypeEnum dishType, Double sellingPrice, List<DishIngredient> dishIngredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = sellingPrice;
        this.dishIngredients = dishIngredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<DishIngredient> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    public Double getDishCost() {
        double totalCost = 0;
        if (dishIngredients != null) {
            for (DishIngredient di : dishIngredients) {
               totalCost += di.getIngredient().getPrice() * di.getQuantityRequired();
            }
        }
        return totalCost;
    }

    public Double getGrossMargin() {
        if (sellingPrice == null) {
            throw new RuntimeException("Le prix de vente est null pour le plat " + name);
        }
        return sellingPrice - getDishCost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) &&
                Objects.equals(name, dish.name) &&
                dishType == dish.dishType &&
                Objects.equals(sellingPrice, dish.sellingPrice) &&
                Objects.equals(dishIngredients, dish.dishIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, sellingPrice, dishIngredients);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", sellingPrice=" + sellingPrice +
                ", dishIngredients=" + dishIngredients +
                '}';
    }
}
