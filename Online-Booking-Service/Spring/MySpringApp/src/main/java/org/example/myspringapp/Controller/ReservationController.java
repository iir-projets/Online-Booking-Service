package org.example.myspringapp.Controller;


import org.example.myspringapp.Model.ReservationRequestDTO;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
import org.example.myspringapp.Service.ReservationService;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReservationController {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    ProductServices productServices;
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;

    @PostMapping("/reservation")
    public Map<String, Object> makeReservation(@RequestBody ReservationRequestDTO requestDTO) {
        Map<String, Object> response = reservationService.makeReservation(requestDTO.getProductName(), requestDTO.getToken());
        System.out.println(response);
        return response;
    }

}
