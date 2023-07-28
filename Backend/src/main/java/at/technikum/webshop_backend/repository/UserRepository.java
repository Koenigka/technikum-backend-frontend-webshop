package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);
    List<User> findByEmailStartingWith(String emailPrefix);

}
