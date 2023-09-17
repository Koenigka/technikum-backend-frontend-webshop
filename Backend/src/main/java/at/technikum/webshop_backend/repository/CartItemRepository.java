package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    CartItem findByUserId(Long userId);


}
