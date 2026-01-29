import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private Double quantity;
    private StockMovementType type;
    private UnitEnum unit;
    private Instant creationDatetime;

    public StockMovement() {}

    public StockMovement(Integer id, Ingredient ingredient, Double quantity,
                         StockMovementType type, UnitEnum unit, Instant creationDatetime) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.type = type;
        this.unit = unit;
        this.creationDatetime = creationDatetime;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public StockMovementType getType() { return type; }
    public void setType(StockMovementType type) { this.type = type; }

    public UnitEnum getUnit() { return unit; }
    public void setUnit(UnitEnum unit) { this.unit = unit; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }
}