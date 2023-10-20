package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.security.UserPrincipal;
import at.technikum.webshop_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@code UserControll} class handles HTTP requests related to user operations.
 * It exposes endpoints for creating users.
 *
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final String authorityAdmin = "ROLE_ADMIN";

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Handles an HTTP POST request to create a new user.
     *
     * @param userDto        The UserDto containing the user information to be registered.
     * @param bindingResult  The BindingResult for validating the userDto.
     * @return A ResponseEntity with the newly created UserDto and an appropriate HTTP status code,
     *         or a bad request status with a validation error message if the userDto is invalid.
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Validation error: Please check your input.");
        }
        User createdUser = userService.save(userDto);
        UserDto createdUserDto = createdUser.convertToDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }


    /**
     * Handles an HTTP POST request to search for users based on specified filters.
     *
     * @param filters A Map containing key-value pairs representing filters for user search.
     *                The keys are filter criteria, and the values are the corresponding filter values.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with a list of UserDto objects matching the specified filters if the requester is an admin.
     *         - 403 Forbidden if the requester does not have admin privileges.
     */
    @PostMapping("/search")
    public ResponseEntity<List<UserDto>> findUsersByFilters(@RequestBody Map<String, String> filters) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            List<UserDto> filteredUsers = userService.findUsersByFilters(filters);
            return ResponseEntity.ok(filteredUsers);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    /**
     * Handles an HTTP GET request to retrieve user information by their ID.
     *
     * @param id The ID of the user to be retrieved.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with the UserDto if the requester is an admin and the user is found.
     *         - 403 Forbidden if the requester does not have admin privileges.
     *         - 404 Not Found if the user with the specified ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            User user = userService.findById(id);
            UserDto userDto = user.convertToDto();
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Handles an HTTP GET request to retrieve the profile information of the currently authenticated user.
     *
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with the UserDto of the authenticated user.
     *         - 403 Forbidden if the user is not authenticated.
     *         - 404 Not Found if the authenticated user's profile is not found.
     */
    @GetMapping("/myProfile")
    public ResponseEntity<UserDto> findById() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getUserId();

            User user = userService.findById(userId);
            if (user != null) {
                UserDto userDto = user.convertToDto();
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }



    /**
     * Handles an HTTP GET request to retrieve a list of users whose email addresses have the specified prefix.
     *
     * @param emailPrefix The prefix to filter user email addresses.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with a list of UserDto objects if the requester is an admin and users are found.
     *         - 403 Forbidden if the requester does not have admin privileges.
     */
    @GetMapping("/findByEmail/{emailPrefix}")
    public ResponseEntity<List<UserDto>> getUsersByEmailPrefix(@PathVariable String emailPrefix) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            List<UserDto> users = userService.getUsersByEmailPrefix(emailPrefix);
            return ResponseEntity.ok(users);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Handles an HTTP GET request to retrieve user information by their email.
     *
     * @param email The email address of the user to be retrieved.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with the UserDto if the requester is an admin and the user with the specified email is found.
     *         - 403 Forbidden if the requester does not have admin privileges.
     *         - 404 Not Found if the user with the specified email is not found.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();



        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                UserDto userDto = user.get().convertToDto();
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Handles an HTTP PUT request to update user information.
     *
     * @param userDto        The UserDto containing the updated user information.
     * @param bindingResult  The BindingResult for validating the userDto.
     * @return A ResponseEntity with the updated UserDto and an appropriate HTTP status code if the requester is an admin,
     *         or a forbidden status if the requester does not have admin privileges,
     *         or a bad request status with a validation error message if the userDto is invalid.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Validation error: Please check your input.");
        }

        User updatedUser = userService.update(userDto);
        UserDto updatedUserDto = updatedUser.convertToDto();
        return ResponseEntity.ok(updatedUserDto);
    }

    /**
     * Handles an HTTP DELETE request to delete a user by their ID.
     *
     * @param id The ID of the user to be deleted.
     * @return A ResponseEntity representing the HTTP response:
     *         - 204 No Content if the requester is an admin and the user is successfully deleted.
     *         - 403 Forbidden if the requester does not have admin privileges.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}