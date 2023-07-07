package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        User createdUser = userService.save(userDto);
        UserDto createdUserDto = createdUser.convertToDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    //TODO Alles auf Response Entity User Dto ändern!

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public Optional<User> findByEmail(@PathVariable String email){
        return userService.findByEmail(email);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        User updatedUser = userService.update(userDto);
        UserDto updatedUserDto = updatedUser.convertToDto();
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteById(id);

    }

}
