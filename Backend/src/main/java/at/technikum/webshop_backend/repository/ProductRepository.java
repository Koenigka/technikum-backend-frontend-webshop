package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Product;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findAll();

    List<Product> findAllById(Long id);

    List<Product> findByCategoryId(int categoryId);

    List<Product> findByTitleContains(String title);

}
