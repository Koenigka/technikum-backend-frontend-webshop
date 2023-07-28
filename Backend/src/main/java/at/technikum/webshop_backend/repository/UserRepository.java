package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


        Optional<User> findByEmail(String email);

        List<User> findByEmailStartingWithAndUsernameContainingAndIsActive(String emailPrefix, String username, Boolean isActive);

        List<User> findByEmailStartingWithAndUsernameContaining(String emailPrefix, String username);

        List<User> findByEmailStartingWithAndIsActive(String emailPrefix, Boolean isActive);

        List<User> findByUsernameContainingAndIsActive(String username, Boolean isActive);

        List<User> findByEmailStartingWith(String emailPrefix);

        List<User> findByUsernameContaining(String username);

        List<User> findByIsActive(Boolean isActive);
    }




