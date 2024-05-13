package org.example.myspringapp.DTO;

import lombok.Data;

@Data
public class ReservationRequestDTO {
    private String token;
    private long price;
    private String productName;

    // getters and setters
}