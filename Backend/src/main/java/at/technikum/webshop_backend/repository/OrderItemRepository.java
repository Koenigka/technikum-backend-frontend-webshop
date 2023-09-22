package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
