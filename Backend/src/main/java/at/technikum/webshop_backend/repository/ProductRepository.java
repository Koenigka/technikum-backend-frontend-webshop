package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Product;
import jakarta.persistence.Column;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByCategoryIdAndActive(Long categoryId, Boolean active);

    List<Product> findAllByActive(Boolean active);

    List<Product> findByActive(Boolean active);

    List<Product> findByTitleContains(String title);

}
