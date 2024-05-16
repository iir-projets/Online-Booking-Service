package org.example.myspringapp.Service;

import com.sun.tools.jconsole.JConsoleContext;
import org.example.myspringapp.DTO.UserDTO;
import org.example.myspringapp.Model.Reservation;
import org.example.myspringapp.Model.User;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
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
    UserRoleRepository userRoleRepository;
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
        504 => Email not Unique
        505 => Phone not Unique

     */

    public Map<String , Object> Registration(UserDTO user ){
        Map<String,Object > response = new HashMap<>();
        // check if the username is unique
        boolean isUsernameUnique = ( userRepository.findByUserName(user.getUserName()) == null );
        boolean isMailUnique = (userRepository.findByEmail(user.getEmail()) == null);
        boolean isPhoneUnique = (userRepository.findByPhone(user.getPhone()) == null);
        boolean isCBUnique = (userRepository.findByCarteBancaire(user.getCarteBancaire()) == null);
        System.out.println("isUsernameUnique \t" + isUsernameUnique );
        System.out.println("isMailUnique \t" + isMailUnique );
        System.out.println("isPhoneUnique \t" + isPhoneUnique );
        System.out.println("isCBUnique \t" + isCBUnique );
        if(!isPhoneUnique || !isMailUnique || !isUsernameUnique || !isCBUnique){
            response.put("isMailUnique",true);
            response.put("isPhoneUnique",true);
            response.put("isUserNameUnique",true);
            response.put("isCBUnique",true);
            if(!isMailUnique){
                response.put("isMailUnique",false);
            }
            if (!isPhoneUnique){
                response.put("isPhoneUnique",false);
            }
            if (!isUsernameUnique){
                response.put("isUserNameUnique",false);
            }
            if(!isCBUnique){
                response.put("isCBUnique",false);
            }
            System.out.println("blocked here ");
            response.put("response",503);
            return response;
        }
        User NewUser = User.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .carteBancaire(user.getCarteBancaire())
                .userRole(userRoleRepository.findById(2L).get())
                .build();
        System.out.println(NewUser);
        userRepository.save(NewUser);
        response.put("response","200");
        String GenratedToken = jwtUtils.generateToken(NewUser);
        response.put("token", GenratedToken);
        System.out.println("GenratedToken"+GenratedToken);
        System.out.println(response);
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



    public Map<String,Object> gethistory(){
        Map<String ,Object> response = new HashMap<>();
        Long Prince = 0L;
        int count = 0;
        int feedback = 0;
        List<Reservation> reservationList = reservationRepository.findAll();
        for(Reservation reservation : reservationList){
            Prince += reservation.getProduct().getPrice();
            count++;
            if(reservation.getComment() != null){
                feedback++;
            }
        }
        response.put("TotalPrice",Prince);
        response.put("data",reservationList);
        response.put("reservations",count);
        response.put("feedback",feedback);
        response.put("response",200);
        return response;
    }
}
