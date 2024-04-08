package org.example.myspringapp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String UserName;
    private String Email;
    private String Password;
    private String Phone;
    private String CarteBancaire;

    @ManyToOne
    @JoinColumn(name = "role_id") // Assumes there's a column 'role_id' in the User table
    private UserRole userRole;

}
