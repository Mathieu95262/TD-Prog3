import java.time.Instant;

public class Product {
    private Integer id;
    private String name;
    private Instant creationDatetime;
    private Category category;

    public Product() {} // constructeur vide

    public Product(Integer id, String name, Instant creationDatetime, Category category) {
        this.id = id;
        this.name = name;
        this.creationDatetime = creationDatetime;
        this.category = category;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public Instant getCreationDatetime() { return creationDatetime; }
    public Category getCategory() { return category; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", category=" + category +
                '}';
    }
}
