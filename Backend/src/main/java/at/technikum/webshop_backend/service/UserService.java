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

/**
 * Service class for managing users, including creation, updating, deletion, and retrieval operations.
 */
@Service
public class UserService {
    private UserRepository userRepository;



    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user from a UserDto.
     *
     * @param userDto The UserDto containing user information.
     * @return The created User.
     */
    public User save(UserDto userDto) {
        User user = new User();
        user.setTitle(userDto.getTitle());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());

        Address address = new Address();
        address.setAddress(userDto.getAddress());
        address.setCity(userDto.getCity());
        address.setZip(userDto.getZip());
        address.setState(userDto.getState());

        user.setAddress(address);

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setIsActive(userDto.getIsActive());
        user.setRoles(userDto.getRoles());

        return userRepository.save(user);
    }

    /**
     * Updates an existing user using information from a UserDto.
     *
     * @param userDto The UserDto containing updated user information.
     * @return The updated User.
     * @throws EntityNotFoundException if the user to update is not found.
     */
    public User update(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userDto.getId()));


        existingUser.setTitle(userDto.getTitle());
        existingUser.setFirstname(userDto.getFirstname());
        existingUser.setLastname(userDto.getLastname());
        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setIsActive(userDto.getIsActive());
        existingUser.setRoles(userDto.getRoles());

        String password = userDto.getPassword();

        if (password != null) {
            existingUser.setPassword(password);
        }

        Address address = existingUser.getAddress();
        address.setAddress(userDto.getAddress());
        address.setCity(userDto.getCity());
        address.setZip(userDto.getZip());
        address.setState(userDto.getState());

        return userRepository.save(existingUser);
    }

    /**
     * Retrieves a list of all users in the database.
     *
     * @return A list of User objects representing all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User if found, or null if not found.
     * @throws EntityNotFoundException if the user is not found.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return An optional containing the User if found, or empty if not found.
     */
    public Optional<User> findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        return userOptional;
    }

    /**
     * Retrieves a list of users whose email addresses start with a given prefix.
     *
     * @param emailPrefix The prefix to filter user email addresses.
     * @return A list of UserDto objects matching the email prefix.
     */
    public List<UserDto> getUsersByEmailPrefix(String emailPrefix) {
        List<User> users = userRepository.findByEmailStartingWith(emailPrefix);
        return users.stream().map(User::convertToDto).collect(Collectors.toList());
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws EntityNotFoundException if the user to delete is not found.
     */
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        userRepository.deleteById(id);
    }

    /**
     * Retrieves a list of users based on specified filters.
     *
     * @param filters A map of filter criteria for querying users.
     * @return A list of UserDto objects matching the filter criteria.
     */
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

    /**
     * Converts a list of User objects to a list of UserDto objects.
     *
     * @param users The list of User objects to convert.
     * @return A list of UserDto objects.
     */
    private List<UserDto> convertToUserDtoList(List<User> users) {
        return users.stream()
                .map(User::convertToDto)
                .collect(Collectors.toList());
    }

}
