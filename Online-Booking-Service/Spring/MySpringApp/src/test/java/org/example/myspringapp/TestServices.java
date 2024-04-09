package org.example.myspringapp;

import com.sun.tools.jconsole.JConsoleContext;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestServices {
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;
    @Mock
    JWTUtils jwtUtils;

    final String token = "e4229a65-d731-4094-820d-6f9bac92944a";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }
    @Test
    public void testAddUser_Successful() {
        // Stubbing userRepository.findAll() to return a list of users
        List<User> users = Arrays.asList();
        when(userRepository.findAll()).thenReturn(users);

        // Call the method that uses userRepository.findAll()
        List<User> fetchedUsers = userRepository.findAll();

        // Verify that the method was called and check the result
        verify(userRepository, times(1)).findAll();
        assertEquals(users.size(), fetchedUsers.size());
    }


}
