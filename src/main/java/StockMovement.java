import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private Double quantity;
    private String type;  // "IN" ou "OUT"
    private UnitEnum unit;
    private Instant creationDatetime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UnitEnum getUnit() { return unit; }
    public void setUnit(UnitEnum unit) { this.unit = unit; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }
}