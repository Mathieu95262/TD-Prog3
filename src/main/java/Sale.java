import java.time.Instant;

public class Sale {
    private Integer id;
    private Order order;
    private Instant saleDatetime;

    public Sale() {}

    public Sale(Integer id, Order order, Instant saleDatetime) {
        this.id = id;
        this.order = order;
        this.saleDatetime = saleDatetime;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Instant getSaleDatetime() { return saleDatetime; }
    public void setSaleDatetime(Instant saleDatetime) { this.saleDatetime = saleDatetime; }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", orderReference=" + (order != null ? order.getReference() : "null") +
                ", saleDatetime=" + saleDatetime +
                '}';
    }
}