package com.example.projectservices;

public class Reservation {
    private String reservationId;
    private String productName;
    private String description;
    private String category;
    private String availability;
    private int price;
    private String location;
    private String reservationDate;

    // Constructor, getters, and setters
    public Reservation(String reservationId, String productName, String description, String category, String availability, int price, String location, String reservationDate) {
        this.reservationId = reservationId;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.availability = availability;
        this.price = price;
        this.location = location;
        this.reservationDate = reservationDate;
    }

    // Getters and setters
    public String getReservationId() { return reservationId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getAvailability() { return availability; }
    public int getPrice() { return price; }
    public String getLocation() { return location; }
    public String getReservationDate() { return reservationDate; }
}
