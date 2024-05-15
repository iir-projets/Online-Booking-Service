package org.example.myspringapp.Repositories;

import org.example.myspringapp.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product,Integer> {
    List<Product> findProductByAvailability(String availability);
    Product findById(long id);

    List<Product> findByCategory(String category);
    List<Product> findByCategory(String category,Pageable pageable);
    List<Product> getAllByCategory(String category,Pageable pageable);
    @Query("SELECT COUNT(b) FROM Reservation b WHERE b.product.id = :productid")
    int countBookingsForProduct(@Param("productid") Long id);

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
