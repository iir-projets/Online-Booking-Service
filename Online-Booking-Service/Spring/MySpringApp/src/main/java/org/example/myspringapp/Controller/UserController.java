package org.example.myspringapp.Controller;

import org.example.myspringapp.DTO.UserDTO;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/add")
    public Map<String, Object> Registration(@RequestBody UserDTO user) {
        return userService.Registration(user);
    }

    @GetMapping("/history")
    public Map<String, Object> getBookingHistory(@RequestParam String token) {
        Map<String, Object> response = userService.getBookingHistory(token);
        return response;
    }
    @GetMapping("/Adminhistory")
    public Map<String, Object> getFullBookingHistory() {
        return userService.gethistory();

    }

    @PostMapping("/authenticate")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> credentials) {
        return userService.authenticateUser(credentials);
    }
    @GetMapping("/checkToken")
    public boolean checkToken(@RequestParam("token") String token) {
        return !jwtUtils.extractExpiration(token).before(new Date());
    }
}