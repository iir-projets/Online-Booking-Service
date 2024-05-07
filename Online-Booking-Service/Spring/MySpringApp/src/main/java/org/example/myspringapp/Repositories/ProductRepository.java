package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product,Integer> {
    List<Product> findProductByAvailability(String availability);

    List<Product> findByCategory(String category);
    List<Product> findByCategory(String category,Pageable pageable);
    List<Product> getAllByCategory(String category,Pageable pageable);

    Product findByName(String name);

    Page<Product> getAllBy(Pageable pageable);
    Page<Product> findAllBy(Pageable pageable);
    List<Product> findByPriceLessThan(Integer price);
    List<Product> findByPriceLessThan(Integer price,Pageable pageable);
    List<Product> getAllByPriceLessThan(Integer price,Pageable pageable);

    // Sort products by price in ascending order
    List<Product> findAllByOrderByPriceAsc();

    // Sort products by price in descending order
    List<Product> findAllByOrderByPriceDesc();

}
