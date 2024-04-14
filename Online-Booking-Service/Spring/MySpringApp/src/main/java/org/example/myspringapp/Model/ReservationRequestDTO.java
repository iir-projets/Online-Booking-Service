package org.example.myspringapp.Model;

import lombok.Data;

@Data
public class ReservationRequestDTO {
    private String token;
    private String productName;

    // getters and setters
}
