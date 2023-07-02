package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));
    }

    public List<Category> findByTitleContains(String title) {
        return categoryRepository.findByTitleContains(title);
    }

    public List<Category> findAllByActive(Boolean active) {
        return categoryRepository.findAllByActive(active);
    }

    public Category update(Long id, Category updatedCategory) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        category.setTitle(updatedCategory.getTitle());
        category.setDescription(updatedCategory.getDescription());
        category.setImgUrl(updatedCategory.getImgUrl());
        category.setActive(updatedCategory.getActive());

        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        categoryRepository.delete(category);
    }
}
