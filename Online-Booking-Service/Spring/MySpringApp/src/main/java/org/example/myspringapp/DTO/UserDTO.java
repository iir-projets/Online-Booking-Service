package org.example.myspringapp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myspringapp.Model.UserRole;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String userName;
    private String email;
    private String password;
    private String phone;
    private String carteBancaire;

}
