package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final String authorityAdmin = "ADMIN";

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        User createdUser = userService.save(userDto);
        UserDto createdUserDto = createdUser.convertToDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

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
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            User updatedUser = userService.update(userDto);
            UserDto updatedUserDto = updatedUser.convertToDto();
            return ResponseEntity.ok(updatedUserDto);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
