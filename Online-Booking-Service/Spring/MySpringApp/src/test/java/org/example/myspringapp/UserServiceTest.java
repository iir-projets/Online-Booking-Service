package org.example.myspringapp;

import org.example.myspringapp.Model.User;
import org.example.myspringapp.Model.UserRole;
import org.example.myspringapp.Repositories.ReservationRepository;
import org.example.myspringapp.Repositories.UserRepository;
import org.example.myspringapp.Repositories.UserRoleRepository;
import org.example.myspringapp.Service.JWTUtils;
import org.example.myspringapp.Service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTUtils jwtUtils;
    @InjectMocks
    private UserService userService;
    @Mock
    UserRoleRepository userRoleRepository;
    @Mock
    ReservationRepository reservationRepository;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Mock userRepository.findByUserName() behavior
        when(userRepository.findByUserName("a43")).thenReturn(new User()); // Mock existing user
        when(userRepository.findByUserName("non-existing")).thenReturn(null); // Mock non-existing user

    }

    @Test
    public void testAddUser_Successful() {
        // Arrange
        UserRole userRole = UserRole.builder()
                .id(4L)
                .roleName("rolename")
                .PrivilegesDescription("description").
                build();
        User NonExistingUser = User.builder()
                .userName("non-existing")
                .email("email")
                .phone("000000")
                .carteBancaire("ccccccccc")
                .userRole(userRole)
                .password("123")
                .build();
        User ExistingUser = User.builder()
                .userName("a43")
                .email("email")
                .phone("000000")
                .carteBancaire("ccccccccc")
                .userRole(userRole)
                .password("123")
                .build();

        // Act
        Map<String, String> response = userService.addUser(ExistingUser);
        Map<String, String> response2 = userService.addUser(NonExistingUser);

        // Assert
        assertEquals("500", response.get("response"));
        assertEquals("200", response2.get("response"));

    }

    @Test
    public void testEditUser(){
        // Arrange
        UserRole userRole = UserRole.builder()
                .id(4L)
                .roleName("rolename")
                .PrivilegesDescription("description").
                build();
        User UserToEdit = User.builder()
                .userName("old-name")
                .email("email")
                .phone("000000")
                .carteBancaire("bank-account")
                .userRole(userRole)
                .password("123")
                .build();
        String token = jwtUtils.generateToken(UserToEdit);

        // Act
        Map<String, String> response = userService.editUser(UserToEdit,token);

        // Assert
        assertEquals("200", response.get("response"));
    }

    @Test
    public void testDeleteUser(){
        // Arrange
        UserRole userRole = UserRole.builder()
                .id(4L)
                .roleName("rolename")
                .PrivilegesDescription("description").
                build();
        User UserToDelete = User.builder()
                .userName("old-name")
                .email("email")
                .phone("000000")
                .carteBancaire("bank-account")
                .userRole(userRole)
                .password("123")
                .build();
        String token = jwtUtils.generateToken(UserToDelete);

        User UserToDelete2 = userRepository.findByUserName("edited");

        // Act
        Map<String, String> response = userService.deleteUser(UserToDelete2,token);

        // Assert
        assertEquals("200", response.get("response"));
    }

    @Test
    public void testgetAllUsers(){
        // Arrange
        UserRole userRole = UserRole.builder()
                .id(4L)
                .roleName("rolename")
                .PrivilegesDescription("description").
                build();
        User UserToDelete = User.builder()
                .userName("old-name")
                .email("email")
                .phone("000000")
                .carteBancaire("bank-account")
                .userRole(userRole)
                .password("123")
                .build();
        String token = jwtUtils.generateToken(UserToDelete);

        User UserToDelete2 = userRepository.findByUserName("edited");

        // Act
        Map<String, Object> response = userService.getAllusers(UserToDelete2,token);

        // Assert
        assertEquals("200", response.get("response"));
    }


    /*
    @Test
    public void testGetBookingHistory(){
        // Arrange
        UserRole userRole = UserRole.builder()
                .id(4L)
                .roleName("rolename")
                .PrivilegesDescription("description").
                build();
        User UserToDelete = User.builder()
                .userName("old-name")
                .email("email")
                .phone("000000")
                .carteBancaire("bank-account")
                .userRole(userRole)
                .password("123")
                .build();
        String token = jwtUtils.generateToken(UserToDelete);

        User UserToDelete2 = userRepository.findByUserName("edited");

        // Act
        Map<String, Object> response = userService.getBookingHistory(UserToDelete2,"token");

        // Assert
        assertEquals(200, response.get("response"));
        System.out.println(response.get("response"));
    }
     */

}
