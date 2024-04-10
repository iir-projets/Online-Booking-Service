package org.example.myspringapp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {
    @Id // id is the Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment
    private Long id ;

    @Column(unique = true)
    private String name ;

    private String description ;
    private String category ;
    private String availability;
    private String Location;
    private int price ;
}
