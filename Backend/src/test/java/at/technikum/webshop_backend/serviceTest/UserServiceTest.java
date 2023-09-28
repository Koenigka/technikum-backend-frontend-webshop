package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.Address;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.UserRepository;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveUser() {
        // Erstelle ein Beispiel-UserDto
        UserDto userDto = new UserDto();
        userDto.setTitle("Mr.");
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setUsername("johndoe");
        userDto.setEmail("johndoe@example.com");
        userDto.setPassword("password");
        userDto.setIsActive(true);

        // Mock das erwartete Verhalten von userRepository.save
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Rufe die Methode save auf
        User resultUser = userService.save(userDto);

        // Überprüfe das Ergebnis
        assertNotNull(resultUser);
        assertEquals("Mr.", resultUser.getTitle());
        assertEquals("John", resultUser.getFirstname());
        assertEquals("Doe", resultUser.getLastname());
        assertEquals("johndoe", resultUser.getUsername());
        assertEquals("johndoe@example.com", resultUser.getEmail());
        assertTrue(resultUser.getIsActive());
    }

    @Test
    void testUpdateUser() {
        // Erstelle ein Beispiel-UserDto
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setTitle("Mrs.");

        // Mock das erwartete Verhalten von userRepository.findById
        User existingUser = new User();
        existingUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Mock das erwartete Verhalten von userRepository.save
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Rufe die Methode update auf
        User resultUser = userService.update(userDto);

        // Überprüfe das Ergebnis
        assertNotNull(resultUser);
        assertEquals("Mrs.", resultUser.getTitle());
    }

    @Test
    void testFindAllUsers() {
        // Erstelle eine Beispiel-Liste von Benutzern
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        // Mock das erwartete Verhalten von userRepository.findAll
        when(userRepository.findAll()).thenReturn(userList);

        // Rufe die Methode findAll auf
        List<User> resultUsers = userService.findAll();

        // Überprüfe das Ergebnis
        assertNotNull(resultUsers);
        assertEquals(2, resultUsers.size());
    }

    @Test
    void testFindUserById() {
        // Erstelle ein Beispiel-Benutzerobjekt
        User user = new User();
        user.setId(1L); // ID eines vorhandenen Benutzers

        // Mock das erwartete Verhalten von userRepository.findById
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Ruf die Methode findById auf
        User resultUser = userService.findById(1L);

        // Überprüfe das Ergebnis
        assertNotNull(resultUser);
        assertEquals(1L, resultUser.getId());
    }

}
