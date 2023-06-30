package at.technikum.webshop_backend.service;

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



    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> findByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        return userOptional;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User update(Long id, User updatedUser){
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setTitle(updatedUser.getTitle());
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());
        user.setAddress(updatedUser.getAddress());
        user.setCity(updatedUser.getCity());
        user.setZip(updatedUser.getZip());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setActive(updatedUser.getActive());
        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }

    public void deleteById(Long id){
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userRepository.deleteById(id);
    }

}
