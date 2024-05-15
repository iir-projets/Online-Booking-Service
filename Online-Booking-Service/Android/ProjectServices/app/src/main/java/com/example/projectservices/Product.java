package com.example.projectservices;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String availability;
    private String location;
    private int price;
    private String imageUrl;
    private byte[] image ;


    public Product(String name, String description, String category, String availability, String location, int price) {
        this.id = id ;
        this.name = name;
        this.description = description;
        this.category = category;
        this.availability = availability;
        this.location = location;
        this.price = price;
    }

    public Product(String name, String description, String category, String availability, String location, int price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.availability = availability;
        this.location = location;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product( String name, String description, String category, String availability, String location, int price, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.availability = availability;
        this.location = location;
        this.price = price;
        this.image = image;
    }



    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
