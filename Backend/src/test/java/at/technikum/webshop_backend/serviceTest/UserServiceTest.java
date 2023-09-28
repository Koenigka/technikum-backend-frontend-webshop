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
        // Create a sample UserDto
        UserDto userDto = new UserDto();
        userDto.setTitle("Mr.");
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setUsername("johndoe");
        userDto.setEmail("johndoe@example.com");
        userDto.setPassword("password");
        userDto.setIsActive(true);
        userDto.setAddress("123 Main St");
        userDto.setCity("New York");
        userDto.setZip(10001);

        // Create a sample User
        User user = new User();
        user.setId(1L);
        user.setTitle("Mr.");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password");
        user.setIsActive(true);
        Address address = new Address();
        address.setAddress("123 Main St");
        address.setCity("New York");
        address.setZip(10001);
        user.setAddress(address);

        // Mock the behavior of userRepository
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the save method
        User resultUser = userService.save(userDto);

        // Verify that userRepository.save() was called once
        verify(userRepository, times(1)).save(any(User.class));

        // Assert the result
        assertNotNull(resultUser);
        assertEquals("Mr.", resultUser.getTitle());
        assertEquals("John", resultUser.getFirstname());
        assertEquals("Doe", resultUser.getLastname());
        assertEquals("johndoe", resultUser.getUsername());
        assertEquals("johndoe@example.com", resultUser.getEmail());
        assertTrue(resultUser.getIsActive());
    }

}