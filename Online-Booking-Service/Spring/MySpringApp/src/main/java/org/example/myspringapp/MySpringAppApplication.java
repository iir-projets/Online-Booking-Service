package org.example.myspringapp;


import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.User;

import org.example.myspringapp.Model.UserRole;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySpringAppApplication implements CommandLineRunner {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(MySpringAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(userRepository.findAll());
        UserRole userRole = userRoleRepository.findById(1L).get();
        User user = new User(null,"testing","user@email.com","123","0000","XXXXXX",userRole);
        User AlteredUser = userRepository.findById(2L).get();
        AlteredUser.setUserName("admin");
        String token = jwtUtils.generateToken(user);

        //System.out.println(userService.addUser(user));
        //System.out.println(userService.editUser(AlteredUser,token));

        User UserToDelete2 = userRepository.findByUserName("edited");
        System.out.println(UserToDelete2);
    }
}
