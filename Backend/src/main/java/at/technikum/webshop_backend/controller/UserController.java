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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final String authorityAdmin = "ROLE_ADMIN";

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Validation error: Please check your input.");
        }
        User createdUser = userService.save(userDto);
        UserDto createdUserDto = createdUser.convertToDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }


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
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      //  boolean isAdmin = authentication.getAuthorities().stream()
         //       .map(GrantedAuthority::toString)
         //       .anyMatch(val -> val.equals(authorityAdmin));
        // Initialize loggedInUserId to null
        Long loggedInUserId = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserPrincipal) {
                loggedInUserId = ((UserPrincipal) principal).getUserId();
            }
        }

        if (id.equals(loggedInUserId)) {
            User user = userService.findById(id);
            if (user != null) {
                UserDto userDto = user.convertToDto();

                // Include the userId in the UserDto
                userDto.setId(id);

                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
      //  if (isAdmin) {
       //     User user = userService.findById(id);
       //     UserDto userDto = user.convertToDto();
       //     return ResponseEntity.ok(userDto);
      //  } else {
      //      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      //  }
    }

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