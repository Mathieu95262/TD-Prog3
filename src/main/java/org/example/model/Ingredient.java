package org.example.model;

import org.example.enums.CategoryEnum;

public class Ingredient {
    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private Double requiredQuantity;


    public Ingredient() {}


    public Ingredient(String name, double price, CategoryEnum category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.requiredQuantity = getRequiredQuantity();
    }

    public Ingredient(String name, double price, CategoryEnum category, Double requiredQuantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.requiredQuantity = null;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public CategoryEnum getCategory() { return category; }
    public void setCategory(CategoryEnum category) { this.category = category; }

    public Double getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(Double requiredQuantity) { this.requiredQuantity = requiredQuantity; }
}
