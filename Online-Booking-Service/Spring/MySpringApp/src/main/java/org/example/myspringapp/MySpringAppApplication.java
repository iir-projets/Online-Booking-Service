package org.example.myspringapp;


import org.example.myspringapp.DTO.UserDTO;
import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.User;

import org.example.myspringapp.Model.UserRole;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
import org.example.myspringapp.Service.ReservationService;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MySpringAppApplication implements CommandLineRunner {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ReservationRepository reservationRepository;

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
    @Autowired
    ReservationService reservationService;

    public static void main(String[] args) {
        SpringApplication.run(MySpringAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*
                # 2nd layer of manual Testing
        */
        //System.out.println(userRepository.findAll());
        UserRole userRole = userRoleRepository.findById(2L).get();
        User user = new User(null,"testing","user@email.com","123","0000","XXXXXX",userRole);
        User AlteredUser = userRepository.findById(2L).get();
        //AlteredUser.setUserName("admin");
        String token = jwtUtils.generateToken(AlteredUser);
        System.out.println(token);
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
        //System.out.println(productServices.sortByPriceASC(token));
        //System.out.println(productServices.sortByPriceDESC(token));
        /*
                #Authentication Test
        Map<String,String> credentials = new HashMap<>();
        credentials.put("email","admin@email.com");
        credentials.put("password","1234");
        System.out.println(userService.authenticateUser(credentials));

         */
        //Product product1 = productRepository.findById(1).get();
        //System.out.println(reservationService.makeReservation("My First Service",token));
        //System.out.println(token);
        //System.out.println(productRepository.findByPriceLessThan(200));

        /*  Testing Registration    */
        UserDTO NewUser = UserDTO.builder()
                .userName("a4343")
                .email("a4343@email.com")
                .password("securePassword")
                .phone("0000003")
                .carteBancaire("XXXXAB")
                .build();
        System.out.println(NewUser);
        //userService.Registration(NewUser);
        System.out.println(productRepository.countBookingsForProduct(productRepository.findById(1).getId()));



    }
}
