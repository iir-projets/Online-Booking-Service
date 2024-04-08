package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product,Integer> {
    List<Product> findProductByCategory(String category);
    List<Product> findProductByAvailability(String availability);
    List<Product> findProductByPriceLessThan(int price);
    List<Product> findByOrderByPriceAsc();
    List<Product> findByOrderByPriceDesc();




    //List<Product>
}
