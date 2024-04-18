package org.example.myspringapp.Controller;


import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ProductController {

    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    ProductServices productServices;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/services")
    public Map<String,Object> getAllProducts(@RequestParam String token){
        Map<String ,Object> response = productServices.sortByPriceDESC(token);
        System.out.println(response);
        return response;
    }

    @PostMapping("/services/category")
    public Map<String,Object> FilterByCategory(@RequestParam String token,@RequestParam String category){
        if(category.equals("")){
            Map<String ,Object> response = productServices.sortByPriceDESC(token);
            System.out.println(response);
            return response;
        }else {
            Map<String ,Object> response = productServices.filterByCategory(category,token);
            System.out.println(response);
            return response;
        }

    }

    @GetMapping("/demandes")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/services/add")
    public Map<String,Object> addProduct(@RequestParam String token,@RequestBody Product product){
        System.out.println("my product is " + product);
        Map<String,Object> response = productServices.addProduct(product,token);
        System.out.println(response);
        //Tested in Postman Successfully ✅
        return response;
    }

    @PostMapping("/services/edit")
    public Map<String,Object> editProduct(@RequestParam String token,@RequestBody Product product){
        Map<String,Object> response = productServices.editProduct(product,token);
        System.out.println(response);
        //Tested in Postman Successfully ✅
        return response;
    }


    @PostMapping("/services/delete")
    public Map<String,Object> deleteProduct(@RequestParam String token,@RequestBody Product product){
        Map<String,Object> response = productServices.deleteProducts(product,token);
        System.out.println(response);
        //Tested in Postman Successfully ✅
        return response;
    }
}
