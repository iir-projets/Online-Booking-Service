package org.example.myspringapp.Service;

import org.example.myspringapp.Model.Reservation;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    ReservationRepository reservationRepository;


    /*
        #Response Sheet
        200 => everything went as planned
        500 => duplicated username
        501 => token not valid
        503 => invalid credentials

     */

    public Map<String , String> addUser(User user ){
        Map<String,String > response = new HashMap<>();
        // check if the username is unique
        Boolean isUsernameUnique = ( userRepository.findByUserName(user.getUserName()) == null );
        if( isUsernameUnique  ){
            userRepository.save(user);
            response.put("response","200");
            userRepository.save(user);
            response.put("token", jwtUtils.generateToken(user));
        }else
            response.put("response","500");

        return  response;
    }

    public Map<String,String> editUser(User user,String token){
        Map<String,String > response = new HashMap<>();
        //check if token is valid
        if (jwtUtils.isTokenExpired(token)){
            response.put("response","501");
            return response;
        }
        userRepository.save(user);
        response.put("response","200");

        return  response;
    }

    public Map<String,String> deleteUser(User user,String token){
        Map<String,String> response = new HashMap<>();

        //chech if token is valid
        if (jwtUtils.isTokenExpired(token)){
            response.put("response","501");
            return response;
        }
        userRepository.delete(user);
        response.put("response","200");

        return response;
    }

    public Map<String, Object> getAllusers(User user, String token){
        Map<String,Object> response = new HashMap<>();

        if (jwtUtils.isTokenExpired(token)){
            response.put("response","501");
            return response;
        }

        List<User> users = userRepository.findAll();
        response.put("data",users);
        response.put("response","200");

        return response;
    }

    public Map<String,Object> getBookingHistory(String token){
        Map<String,Object> response = new HashMap<>();
        System.out.println("token =" + token);
        if(jwtUtils.isTokenExpired(token)){
            response.put("response",501);
            return response;
        }
        String username = jwtUtils.extractUserName(token);
        System.out.println(username);
        User user = userRepository.findByUserName(username);
        List<Reservation> reservationList = reservationRepository.findByUser(user);
        response.put("response",200);
        response.put("data",reservationList);

        return response;
    }

    public Map<String, String> authenticateUser(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        // Retrieve user from database
        User user = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();
        if (user != null && password.equals(user.getPassword())) {
            // Generate JWT token
            String token = jwtUtils.generateToken(user);
            // Return the token to the client

            response.put("token", token);
            String role = user.getUserRole().getRoleName();
            response.put("role",role);
            response.put("response","200");
            return response;
        } else {
            response.put("response","503");
            //throw new RuntimeException("Invalid credentials");
            return response;
        }
    }
}
