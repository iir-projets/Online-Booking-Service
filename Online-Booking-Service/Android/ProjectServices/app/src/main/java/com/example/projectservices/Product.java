package com.example.projectservices;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String availability;
    private String location;
    private int price;


    public Product(String name, String description, String category, String availability, String location, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.availability = availability;
        this.location = location;
        this.price = price;
    }


    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
