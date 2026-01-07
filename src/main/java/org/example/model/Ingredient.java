package org.example.model;

import org.example.enums.CategoryEnum;

public class Ingredient {
    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;

    public Ingredient() {}

    public Ingredient(String name, double price, CategoryEnum category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public CategoryEnum getCategory() { return category; }
    public void setCategory(CategoryEnum category) { this.category = category; }
}

