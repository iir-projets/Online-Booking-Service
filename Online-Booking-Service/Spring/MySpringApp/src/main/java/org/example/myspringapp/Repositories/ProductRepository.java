package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product,Integer> {
    List<Product> findProductByAvailability(String availability);

    List<Product> findByCategory(String category);

    Product findByName(String name);


    List<Product> findByPriceLessThan(Integer price);

    // Sort products by price in ascending order
    List<Product> findAllByOrderByPriceAsc();

    // Sort products by price in descending order
    List<Product> findAllByOrderByPriceDesc();

}
