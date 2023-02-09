package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.UserRepository;
import at.technikum.webshop_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.save(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public List<User> findByEmail(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteById(id);

    }

}
