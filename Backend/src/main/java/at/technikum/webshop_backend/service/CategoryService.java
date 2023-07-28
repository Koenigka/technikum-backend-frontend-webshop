package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setImgUrl(categoryDto.getImgUrl());
        category.setActive(categoryDto.getActive());
        return categoryRepository.save(category);
    }


    public Category updateCategory(CategoryDto updatedCategoryDto) {

        Category category = categoryRepository.findById(updatedCategoryDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + updatedCategoryDto.getId()));

        category.setTitle(updatedCategoryDto.getTitle());
        category.setDescription(updatedCategoryDto.getDescription());
        category.setImgUrl(updatedCategoryDto.getImgUrl());
        category.setActive(updatedCategoryDto.getActive());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        categoryRepository.delete(category);
    }


    public List<CategoryDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(Category::convertToDto)
                .collect(Collectors.toList());
    }
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));
    }

    public List<CategoryDto> findAllCategoriesByActive(Boolean active) {
        List<Category> categories = categoryRepository.findAllByActive(active);
        return categories.stream()
                .map(Category::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> findCategoriesByFilter(Map<String, String> filters) {
        String categoryTitle = filters.get("filter[title]");
        String active = filters.get("filter[active]");


        List<Category> categories;

        if (categoryTitle != null && active != null) {
            categories = categoryRepository.findByTitleContainsAndActive(categoryTitle, Boolean.parseBoolean(active));
        } else if (categoryTitle != null) {
            categories = categoryRepository.findByTitleContains(categoryTitle);
        } else if (active != null) {
            categories = categoryRepository.findByActive(Boolean.parseBoolean(active));
        } else {
            categories = categoryRepository.findAll();
        }

        return convertToCategoryDtoList(categories);
    }

    private List<CategoryDto> convertToCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(Category::convertToDto)
                .collect(Collectors.toList());
    }

}
