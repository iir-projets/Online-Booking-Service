package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.User;
import org.example.myspringapp.Model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUserName(String userName);
    public User findByEmail(String email);
    public  User findByPhone(String phone);
    public  User findByCarteBancaire(String Carte);
}
