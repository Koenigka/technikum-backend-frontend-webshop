package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.model.Address;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        //TODO - proof active Frontend Backend
        System.out.println("IsActive: " + userDto.getIsActive());

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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public Optional<User> findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        return userOptional;
    }

    public List<UserDto> getUsersByEmailPrefix(String emailPrefix) {
        List<User> users = userRepository.findByEmailStartingWith(emailPrefix);
        return users.stream().map(User::convertToDto).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        userRepository.deleteById(id);
    }

    public List<UserDto> findUsersByFilters(Map<String, String> filters) {
        String emailPrefix = filters.get("email");
        String username = filters.get("username");
        String isActive = filters.get("active");

        List<User> users;

        if (emailPrefix != null && username != null && isActive != null) {
            users = userRepository.findByEmailStartingWithAndUsernameContainingAndIsActive(emailPrefix, username, Boolean.parseBoolean(isActive));
        } else if (emailPrefix != null && username != null) {
            users = userRepository.findByEmailStartingWithAndUsernameContaining(emailPrefix, username);
        } else if (emailPrefix != null && isActive != null) {
            users = userRepository.findByEmailStartingWithAndIsActive(emailPrefix, Boolean.parseBoolean(isActive));
        } else if (username != null && isActive != null) {
            users = userRepository.findByUsernameContainingAndIsActive(username, Boolean.parseBoolean(isActive));
        } else if (emailPrefix != null) {
            users = userRepository.findByEmailStartingWith(emailPrefix);
        } else if (username != null) {
            users = userRepository.findByUsernameContaining(username);
        } else if (isActive != null) {
            users = userRepository.findByIsActive(Boolean.parseBoolean(isActive));
        } else {
            users = userRepository.findAll();
        }

        return convertToUserDtoList(users);
    }
    private List<UserDto> convertToUserDtoList(List<User> users) {
        return users.stream()
                .map(User::convertToDto)
                .collect(Collectors.toList());
    }

}
