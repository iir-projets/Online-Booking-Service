package org.example.myspringapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String carteBancaire;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties("users")// Assumes there's a column 'role_id' in the User table
    private UserRole userRole;

}
