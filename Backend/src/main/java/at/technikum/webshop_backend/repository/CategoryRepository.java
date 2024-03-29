package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findAllByActive(Boolean active);


    List<Category> findByTitleContains(String title);

    List<Category> findByActive(Boolean active);

    List<Category> findByTitleContainsAndActive(String title, Boolean active);
}


