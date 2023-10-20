package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing categories.
 * This class handles operations such as creating and updating categories.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new category based on the provided CategoryDto.
     *
     * @param categoryDto The CategoryDto containing the information for creating a category.
     * @return The newly created Category object.
     */
    public Category createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setImgUrl(categoryDto.getImgUrl());
        category.setActive(categoryDto.getActive());
        return categoryRepository.save(category);
    }


    /**
     * Updates an existing category based on the provided updated CategoryDto.
     *
     * @param updatedCategoryDto The updated CategoryDto containing the new information for the category.
     * @return The updated Category object.
     * @throws EntityNotFoundException if the category specified by updatedCategoryDto's ID is not found.
     */
    public Category updateCategory(CategoryDto updatedCategoryDto) {

        Category category = categoryRepository.findById(updatedCategoryDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + updatedCategoryDto.getId()));

        category.setTitle(updatedCategoryDto.getTitle());
        category.setDescription(updatedCategoryDto.getDescription());
        category.setImgUrl(updatedCategoryDto.getImgUrl());
        category.setActive(updatedCategoryDto.getActive());

        return categoryRepository.save(category);
    }

    /**
     * Deletes an existing category based on the provided category ID.
     *
     * @param id The ID of the category to be deleted.
     * @throws EntityNotFoundException if the category specified by id is not found.
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));

        categoryRepository.delete(category);
    }


    /**
     * Retrieves and returns a list of all categories.
     *
     * @return A list of CategoryDto objects representing all categories.
     */
    public List<CategoryDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(Category::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return The Category object with the specified ID.
     * @throws EntityNotFoundException if the category with the specified ID is not found.
     */
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));
    }

    /**
     * Retrieves and returns a list of categories based on their active status.
     *
     * @param active The active status of categories to filter by.
     * @return A list of CategoryDto objects representing categories with the specified active status.
     */
    public List<CategoryDto> findAllCategoriesByActive(Boolean active) {
        List<Category> categories = categoryRepository.findAllByActive(active);
        return categories.stream()
                .map(Category::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves and returns a list of categories based on the specified filter criteria.
     *
     * @param filters A map of filter criteria where "filter[title]" represents the title filter
     *                and "filter[active]" represents the active status filter.
     * @return A list of CategoryDto objects representing categories that match the specified filter criteria.
     */
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
