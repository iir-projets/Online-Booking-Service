package org.example.myspringapp;


import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.User;

import org.example.myspringapp.Model.UserRole;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
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
    @Autowired
    ProductServices productServices;

    public static void main(String[] args) {
        SpringApplication.run(MySpringAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*
                # 2nd layer of manual Testing
        */
        System.out.println(userRepository.findAll());
        UserRole userRole = userRoleRepository.findById(1L).get();
        User user = new User(null,"testing","user@email.com","123","0000","XXXXXX",userRole);
        User AlteredUser = userRepository.findById(2L).get();
        //AlteredUser.setUserName("admin");
        String token = jwtUtils.generateToken(AlteredUser);

        //System.out.println(userService.addUser(user));
        //System.out.println(userService.editUser(AlteredUser,token));

        //User UserToDelete2 = userRepository.findByUserName("edited");
        //System.out.println(UserToDelete2);

        //System.out.println(userService.getBookingHistory(AlteredUser,token));

        Product product = Product.builder()
                .availability("true")
                .category("category 1")
                .description("description of this product edited vol 2")
                .id(null)
                .Location("Marrakech")
                .name("the Service I added")
                .price(200)
                .build();
        //System.out.println(productServices.editProduct(product,token));
        System.out.println(productServices.sortByPriceASC(token));
        System.out.println(productServices.sortByPriceDESC(token));

    }
}