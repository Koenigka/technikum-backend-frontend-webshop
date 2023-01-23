package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();

    List<Category> findAllByType(String type);
}
