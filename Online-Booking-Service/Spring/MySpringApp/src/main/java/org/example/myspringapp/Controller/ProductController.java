package org.example.myspringapp.Controller;


import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;


@Controller
public class ProductController {
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    ProductServices productServices;

    @PostMapping("/services")
    public Map<String,Object> getAllProducts(@RequestBody String token){
        System.out.println("bug");
        Map<String ,Object> response = productServices.sortByPriceDESC(token);
        return response;
    }
}
