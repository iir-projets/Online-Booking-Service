package org.example.myspringapp;


import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.User;

import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.UserRepository;
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

    public static void main(String[] args) {
        SpringApplication.run(MySpringAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
