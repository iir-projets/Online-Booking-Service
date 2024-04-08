package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findById(long id);
}
