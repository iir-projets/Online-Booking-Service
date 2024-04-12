package org.example.myspringapp.Controller;


import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ProductController {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    ProductServices productServices;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/services")
    public Map<String,Object> getAllProducts(@RequestBody String token){
        System.out.println("bug");
        Map<String ,Object> response = productServices.sortByPriceDESC(token);
        return response;
    }
    @PostMapping("/authenticate")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> credentials) {
        return userService.authenticateUser(credentials);
    }

    @GetMapping("/users")
    public List<User> getusers(){
        return userRepository.findAll();
    }
}
