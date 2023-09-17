package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByUserId(Long userId);


    CartItem findByUserAndProduct(User user, Optional<Product> product);

    List<CartItem> findByUser(User user);
}
