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
    private int Rating ;
    private int price ;




    @Lob
    @Basic(fetch = FetchType.LAZY) // Make the image attribute lazy-loaded
    @Column(length = 1_000_000) // Adjust the length as per your requirement
    private byte[] image;

}
