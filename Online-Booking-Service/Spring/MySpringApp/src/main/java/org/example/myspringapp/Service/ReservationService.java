package org.example.myspringapp.Service;


import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.Reservation;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReservationService {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductServices productServices;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    ReservationRepository reservationRepository;

    /*
        #Response Sheet
        200 => everything went as planned
        404 => resource cant be found
        500 => duplicated name
        501 => token not valid
        502 => product not found
    */

    public Map<String,Object> makeReservation(String  productName , String token){
        Map<String,Object> response = new HashMap<>();

        //1st step => check if token is valid
        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }

        //2nd step => Get the user that made this booking
        User user = userRepository.findByUserName(jwtUtils.extractUserName(token));

        //3rd step => get the product if exist
        Product product = productRepository.findByName(productName);
        if(product == null){
            response.put("response" , 502);
            return response;
        }
        Reservation reservation = Reservation.builder()
                .id(null)
                .product(product)
                .reservationDate(new Date())
                .user(user)
                .build();
        reservationRepository.save(reservation);
        response.put("data",reservation);
        response.put("response",200);
        return response;



    }


}
