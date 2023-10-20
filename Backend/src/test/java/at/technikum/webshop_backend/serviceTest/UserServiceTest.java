package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.Address;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.UserRepository;
import at.technikum.webshop_backend.service.EntityNotFoundException;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testSaveUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setTitle("Herr");
        userDto.setFirstname("Max");
        userDto.setLastname("Mustermann");
        userDto.setAddress_id(123L);
        userDto.setAddress("Musterstraße 123");
        userDto.setCity("Musterstadt");
        userDto.setState("Musterland");
        userDto.setZip(1234);
        userDto.setUsername("max123");
        userDto.setPassword("Passwort123");
        userDto.setEmail("max.mustermann@example.com");
        userDto.setIsActive(true);


        User user = new User();
        user.setId(userDto.getId());
        user.setTitle(userDto.getTitle());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());

        Address address = new Address();
        address.setId(userDto.getAddress_id());
        address.setAddress(userDto.getAddress());
        address.setCity(userDto.getCity());
        address.setState(userDto.getState());
        address.setZip(userDto.getZip());
        user.setAddress(address);

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setIsActive(userDto.getIsActive());

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.save(userDto);

        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getTitle(), createdUser.getTitle());
        assertEquals(user.getFirstname(), createdUser.getFirstname());
        assertEquals(user.getLastname(), createdUser.getLastname());
    }

    @Test
    public void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setTitle("Herr");
        userDto.setFirstname("Neuer Vorname");
        userDto.setLastname("Neuer Nachname");
        userDto.setAddress_id(123L);
        userDto.setAddress("Neue Adresse");
        userDto.setCity("Neue Stadt");
        userDto.setState("Neues Land");
        userDto.setZip(6789);
        userDto.setUsername("neuerUsername");
        userDto.setPassword("NeuesPasswort123");
        userDto.setEmail("neu@example.com");
        userDto.setIsActive(false);

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setTitle("Herr");
        existingUser.setFirstname("Max");
        existingUser.setLastname("Mustermann");
        Address originalAddress = new Address();
        originalAddress.setId(123L);
        originalAddress.setAddress("Musterstraße 123");
        originalAddress.setCity("Musterstadt");
        originalAddress.setState("Musterland");
        originalAddress.setZip(1234);
        existingUser.setAddress(originalAddress);
        existingUser.setUsername("max123");
        existingUser.setPassword("Passwort123");
        existingUser.setEmail("max.mustermann@example.com");
        existingUser.setIsActive(true);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.update(userDto);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(userDto.getId(), updatedUser.getId());
        assertEquals(userDto.getTitle(), updatedUser.getTitle());
        assertEquals(userDto.getFirstname(), updatedUser.getFirstname());
        assertEquals(userDto.getLastname(), updatedUser.getLastname());
        assertEquals(userDto.getAddress_id(), updatedUser.getAddress().getId());
        assertEquals(userDto.getAddress(), updatedUser.getAddress().getAddress());
        assertEquals(userDto.getCity(), updatedUser.getAddress().getCity());
        assertEquals(userDto.getState(), updatedUser.getAddress().getState());
        assertEquals(userDto.getZip(), updatedUser.getAddress().getZip());
        assertEquals(userDto.getUsername(), updatedUser.getUsername());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals(userDto.getIsActive(), updatedUser.getIsActive());
    }


    @Test
    public void testUpdateUserNotFound() {
        UserDto userDto = new UserDto();
        userDto.setId(100L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.update(userDto);
        });

        assertEquals("User not found with id: " + userDto.getId() + " not found.", exception.getMessage()) ;
    }


    @Test
    public void testFindAllUsers() {
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setTitle("Herr");
        user1.setFirstname("Max");
        user1.setLastname("Mustermann");

        Address address1 = new Address();
        address1.setId(123L);
        address1.setAddress("Musterstraße 123");
        address1.setCity("Musterstadt");
        address1.setState("Musterland");
        address1.setZip(1234);

        user1.setAddress(address1);

        user1.setUsername("max123");
        user1.setPassword("Passwort123");
        user1.setEmail("max.mustermann@example.com");
        user1.setIsActive(true);

        userList.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setTitle("Frau");
        user2.setFirstname("Maria");
        user2.setLastname("Musterfrau");

        Address address2 = new Address();
        address2.setId(456L);
        address2.setAddress("Musterweg 456");
        address2.setCity("Musterstadt");
        address2.setState("Musterland");
        address2.setZip(6789);

        user2.setAddress(address2);

        user2.setUsername("maria456");
        user2.setPassword("Passwort456");
        user2.setEmail("maria.musterfrau@example.com");
        user2.setIsActive(true);

        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> allUsers = userService.findAll();

        verify(userRepository, times(1)).findAll();

        assertEquals(userList.size(), allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }

}
