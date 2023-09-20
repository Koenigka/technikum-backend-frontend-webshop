package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.Product;
import jakarta.persistence.Column;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryAndActive(Category category, Boolean active);

    List<Product> findByCategoryIdAndActive(Long categoryId, Boolean active);


    List<Product> findByActive(Boolean active);

    List<Product> findByTitleContains(String title);

    List<Product> findByCategory(Category category);

    List<Product> findByTitleContainsAndCategory(String title, Category category);

    List<Product> findByTitleContainsAndActive(String title, Boolean active);


    List<Product> findByTitleContainsAndCategoryAndActive(String title, Category category, Boolean active);
}
