package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Cart;
import at.technikum.webshop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

    Cart findByUser(User user);


}
