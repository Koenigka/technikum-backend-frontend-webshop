package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping("/{email}")
    public Optional<User> findByEmail(@PathVariable String email, @RequestBody @Valid User user){
        return userService.findByEmail(email);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody @Valid User user){
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteById(id);

    }

}
