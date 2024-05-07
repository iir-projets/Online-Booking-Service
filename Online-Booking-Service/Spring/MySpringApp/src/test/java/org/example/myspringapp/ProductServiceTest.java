package org.example.myspringapp;

import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ProductServices;
import org.example.myspringapp.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    private ProductServices productService;
    @InjectMocks
    private UserService userService;
    @Mock
    UserRoleRepository userRoleRepository;
    @Mock
    ReservationRepository reservationRepository;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEditProduct() {
        // Arrange
        Product existingProduct = Product.builder()
                .id(1L)
                .name("Existing Product")
                .description("Description")
                .category("Category")
                .availability("Available")
                .Location("Location")
                .price(100)
                .build();
        when(productRepository.findByName(existingProduct.getName())).thenReturn(existingProduct);
        when(jwtUtils.isTokenExpired(anyString())).thenReturn(false);

        // Act
        Map<String, Object> response = productService.editProduct(existingProduct, "valid_token");

        // Assert
        assertEquals(200, response.get("response"));
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testFilterByCategory() {
        // Arrange
        String category = "Category";
        when(productRepository.findByCategory(category)).thenReturn(List.of(
                new Product(1L, "Product 1", "Description", category, "Available", "Location", 100),
                new Product(2L, "Product 2", "Description", category, "Available", "Location", 150)
        ));
        when(jwtUtils.isTokenExpired(anyString())).thenReturn(false);

        // Act
        Map<String, Object> response = productService.filterByCategory(category,"valid_token",1);


        // Assert
        assertEquals(200, response.get("response"));
        assertEquals(2, ((List<Product>) response.get("data")).size());
    }

    @Test
    public void testSortByPriceASC() {
        // Arrange
        when(productRepository.findAllByOrderByPriceAsc()).thenReturn(List.of(
                new Product(1L, "Product 1", "Description", "Category", "Available", "Location", 100),
                new Product(2L, "Product 2", "Description", "Category", "Available", "Location", 150)
        ));
        when(jwtUtils.isTokenExpired(anyString())).thenReturn(false);

        // Act
        Map<String, Object> response = productService.sortByPriceASC("valid_token");

        // Assert
        assertEquals(200, response.get("response"));
        assertEquals(2, ((List<Product>) response.get("data")).size());
    }

    @Test
    public void testSortByPriceDESC() {
        // Arrange
        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(List.of(
                new Product(1L, "Product 1", "Description", "Category", "Available", "Location", 100),
                new Product(2L, "Product 2", "Description", "Category", "Available", "Location", 150)
        ));
        when(jwtUtils.isTokenExpired(anyString())).thenReturn(false);

        // Act
        Map<String, Object> response = productService.sortByPriceDESC("valid_token");

        // Assert
        assertEquals(200, response.get("response"));
        assertEquals(2, ((List<Product>) response.get("data")).size());
    }




}
