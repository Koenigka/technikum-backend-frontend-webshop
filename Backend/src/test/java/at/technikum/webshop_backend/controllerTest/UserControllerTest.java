package at.technikum.webshop_backend.controllerTest;

import at.technikum.webshop_backend.controller.UserController;
import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.Address;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }


    private User createTestUser() {
        User user = new User();
        user.setId(1L); // Set the ID
        user.setUsername("TestUser");
        user.setEmail("test@example.com");
        user.setTitle("Mr");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPassword("password");
        user.setIsActive(true);
        user.setRoles("ROLE_ADMIN");

        // Create an Address object and set its properties
        Address address = new Address();
        address.setId(1L);
        address.setAddress("123 Main St");
        address.setCity("Vienna");
        address.setState("Vienna");
        address.setZip(1160);

        // Set the Address object in the User
        user.setAddress(address);

        // You can set other properties as needed for your test

        return user;
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindByEmailAsAdmin() throws Exception {
        // Mock a user object and its associated UserDto for testing
        User testUser = createTestUser(); // Implement this method to create a test user
        when(userService.findByEmail(any(String.class))).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/email/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));
    }

    @Test
    @WithMockUser // A non-admin user, without the ADMIN authority
    public void testFindByEmailAsNonAdmin() throws Exception {
        mockMvc.perform(get("/api/users/email/{email}", "test@example.com"))
                .andExpect(status().isForbidden());
    }@Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testUpdateUserAsAdmin() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L); // Set the ID of the user you want to update
        userDto.setUsername("UpdatedUser");
        userDto.setEmail("updated@example.com");
        userDto.setTitle("Mr"); // Set the title if needed
        userDto.setFirstname("John"); // Set the first name if needed
        userDto.setLastname("Doe"); // Set the last name if needed
        userDto.setPassword("password"); // Set the password
        userDto.setIsActive(true); // Set user's active status
        userDto.setRoles("ROLE_ADMIN"); // Set user's roles, e.g., "ADMIN", "USER"

        User updatedUser =  new User();
        updatedUser.setId(1L); // Set the ID
        updatedUser.setUsername("UpdatedUser");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setTitle("Mr");
        updatedUser.setFirstname("John");
        updatedUser.setLastname("Doe");
        updatedUser.setPassword("password");
        updatedUser.setIsActive(true);
        updatedUser.setRoles("ROLE_ADMIN");

        // Create an Address object and set its properties
        Address address = new Address();
        address.setId(1L);
        address.setAddress("123 Main St");
        address.setCity("Vienna");
        address.setState("Vienna");
        address.setZip(1160);

        // Set the Address object in the User
        updatedUser.setAddress(address);

        // Mock the userService behavior
        when(userService.update(any(UserDto.class))).thenReturn(updatedUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto); // Serialize userDto

        // Perform the PUT request
        mockMvc.perform(put("/api/users/update")
                        .content(userDtoJson) // Use the serialized userDto as the request content
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("UpdatedUser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER") // Test for a non-admin user
    public void testDeleteUserAsNonAdmin() throws Exception {
        Long userIdToDelete = 1L;

        // Perform the DELETE request
        mockMvc.perform(delete("/api/users/delete/{id}", userIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindUserByIdAsAdmin() throws Exception {
        Long userIdToFind = 1L; // Replace with the actual ID of the user you want to find

        // Mock the userService behavior to return a User object
        User user = createTestUser(); // Implement this method to create a test user
        when(userService.findById(userIdToFind)).thenReturn(user);

        // Perform the GET request
        mockMvc.perform(get("/api/users/{id}", userIdToFind)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }


}
