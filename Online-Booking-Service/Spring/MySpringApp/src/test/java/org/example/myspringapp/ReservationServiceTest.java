package org.example.myspringapp;

import org.example.myspringapp.Controller.ReservationController;
import org.example.myspringapp.Model.Product;
import org.example.myspringapp.Model.Reservation;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.ProductRepository;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {
    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testMakeReservation_ValidTokenAndProductExists() {
        // Arrange
        String productName = "My First Service";
        String token = "validToken";

        User user = new User();
        user.setUserName("testUser");

        Product product = new Product();
        product.setName(productName);

        Reservation reservation = new Reservation();

        // Mocking the scenario when it works as planned
        when(jwtUtils.isTokenExpired(token)).thenReturn(false);
        when(jwtUtils.extractUserName(token)).thenReturn("testUser");
        when(userRepository.findByUserName(anyString())).thenReturn(user);
        when(productRepository.findByName(productName)).thenReturn(product);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Map<String, Object> response = reservationService.makeReservation(productName, token);
        System.out.println("Response: " + response); // Add this line for logging

        // Assert
        assertEquals(200, response.get("response"));
        assertEquals(reservation, response.get("data"));

        // Verify interactions
        verify(jwtUtils).isTokenExpired(token);
        verify(jwtUtils).extractUserName(token);
        verify(userRepository).findByUserName("testUser");
        verify(productRepository).findByName(productName);
        verify(reservationRepository).save(any(Reservation.class));
    }

}
