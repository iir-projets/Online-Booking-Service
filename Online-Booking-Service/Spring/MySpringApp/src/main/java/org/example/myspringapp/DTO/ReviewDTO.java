package org.example.myspringapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myspringapp.Model.Reservation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Reservation reservation;
    private int Rating ;
    private String Commentinput ;
}
