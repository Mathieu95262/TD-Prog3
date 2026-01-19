package org.example.model;

import java.time.Instant;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private Instant creationDatetime;
    private Category category;

    public Product(int id, String name, Instant creationDatetime, Category category) {
        this.id = id;
        this.name = name;
        this.creationDatetime = creationDatetime;
        this.category = category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }
    public List<String> getCategoryNames() {
        return List.of(category.getName());
    }
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', creationDatetime=" + creationDatetime + ", category=" + category + "}";
    }
}
