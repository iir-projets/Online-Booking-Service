package org.example.myspringapp.Service;

import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServices {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    JWTUtils jwtUtils;

    /*
        #Response Sheet
        200 => everything went as planned
        404 => resource cant be found
        500 => duplicated name
        501 => token not valid
        502 => product not found
    */

    /*
            #CRUD operations implemented
    */

    public Map<String,Object> addProduct(Product product ,String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        //check if the name is unique
        if(productRepository.findByName(product.getName()) != null){
            response.put("response",500);
            return response;

        }
        //assuming that all fields in Product are full
        productRepository.save(product);
        response.put("response",200);

        return response;
    }

    public Map<String,Object> deleteProducts(Product product , String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }

        if (productRepository.findByName(product.getName()) == null){
            response.put("response",502);
            return response;
        }
        response.put("response",200);
        productRepository.delete(productRepository.findByName(product.getName()));

        return response;
    }

    public Map<String,Object> editProduct(Product product , String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        Product existingProduct = productRepository.findByName(product.getName());
        if (existingProduct == null){
            response.put("response",502);
            return response;
        }else {
            existingProduct.setAvailability(product.getAvailability());
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setLocation(product.getLocation());
            existingProduct.setPrice(product.getPrice());
            response.put("response",200);
            productRepository.save(existingProduct);

        }


        return response;

    }

    public Map<String,Object> filterByCategory(String category , String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        List<Product> productList = productRepository.findByCategory(category);
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);

        return response;
    }

    public Map<String,Object> sortByPriceASC(String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        List<Product> productList = productRepository.findAllByOrderByPriceAsc();
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);

        return response;
    }

    public Map<String,Object> sortByPriceDESC(String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        List<Product> productList = productRepository.findAllByOrderByPriceDesc();
        if (productList == null){
            response.put("response" , 404);
            return response;
        }
        response.put("response",200);
        response.put("data",productList);

        return response;
    }
}
