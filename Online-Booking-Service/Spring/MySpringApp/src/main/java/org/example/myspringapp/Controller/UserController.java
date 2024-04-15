package org.example.myspringapp.Controller;

import org.example.myspringapp.Model.User;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody User user) {
        Map<String, String> response = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String, String>> editUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        Map<String, String> response = userService.editUser(user, token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        Map<String, String> response = userService.deleteUser(user, token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestHeader("Authorization") String token) {
        User  user = new User();
        Map<String, Object> response = userService.getAllusers(  user , token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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