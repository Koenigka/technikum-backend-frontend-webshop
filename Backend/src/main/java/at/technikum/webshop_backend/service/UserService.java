package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.Address;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;



    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User save(UserDto userDto) {
        User user = new User();
        user.setTitle(userDto.getTitle());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());

        Address address = new Address();
        address.setAddress(userDto.getAddress());
        address.setCity(userDto.getCity());
        address.setZip(userDto.getZip());

        user.setAddress(address);

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setIsActive(userDto.getIsActive());
        user.setRole(userDto.getRole());

        return userRepository.save(user);
    }

    public User update(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userDto.getId()));

        existingUser.setTitle(userDto.getTitle());
        existingUser.setFirstname(userDto.getFirstname());
        existingUser.setLastname(userDto.getLastname());
        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setIsActive(userDto.getIsActive());
        existingUser.setRole(userDto.getRole());

        Address address = existingUser.getAddress();
        address.setAddress(userDto.getAddress());
        address.setCity(userDto.getCity());
        address.setZip(userDto.getZip());

        return userRepository.save(existingUser);
    }


    public List<User> findAll(){
        return userRepository.findAll();
    }


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }


    public Optional<User> findByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        return userOptional;
    }


    public void deleteById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        userRepository.deleteById(id);
    }

}
