package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.CustomerOrder;
import at.technikum.webshop_backend.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {


    List<CustomerOrder> findByUser(User user);
}
