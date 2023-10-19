package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code CategoryController} class handles HTTP requests related to category operations.
 * It exposes endpoints for creating categories.
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private static final String authorityAdmin = "ROLE_ADMIN";

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    /**
     * Handles an HTTP POST request to create a new category.
     *
     * @param categoryDto   The DTO (Data Transfer Object) representing the new category.
     * @param bindingResult The result of data binding and validation.
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Validation error: Please check your input.");
        }
        Category createdCategory = categoryService.createCategory(categoryDto);
        CategoryDto newCategoryDto = createdCategory.convertToDto();
        return ResponseEntity.ok(newCategoryDto);
    }


    /**
     * Handles an HTTP PUT request to update an existing category.
     *
     * @param updatedCategoryDto The DTO (Data Transfer Object) representing the updated category.
     * @param bindingResult      The result of data binding and validation.
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CategoryDto updatedCategoryDto, BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        if (bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> errorMessages = fieldErrors.stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }

        Category updatedCategory = categoryService.updateCategory(updatedCategoryDto);
        CategoryDto categoryDto = updatedCategory.convertToDto();
        return ResponseEntity.ok(categoryDto);
    }

    /**
     * Handles an HTTP DELETE request to delete a category by its ID.
     *
     * @param id The ID of the category to be deleted.
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Handles an HTTP GET request to retrieve a list of all categories.
     *
     * @return A list of CategoryDto representing all categories.
     */
    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAllCategories();
    }


    /**
     * Handles an HTTP GET request to retrieve a category by its ID.
     *
     * @param id The ID of the category to be retrieved.
     * @return A ResponseEntity with the retrieved CategoryDto and an appropriate HTTP status code.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        CategoryDto categoryDto = category.convertToDto();
        return ResponseEntity.ok(categoryDto);
    }


    /**
     * Handles an HTTP GET request to retrieve a list of categories filtered by their active status.
     *
     * @param active A boolean value indicating whether to retrieve active or inactive categories.
     * @return A list of CategoryDto representing categories filtered by their active status.
     */
    @GetMapping("/isActive/{active}")
    public List<CategoryDto> findAllCategoriesByActive(@PathVariable Boolean active) {
        return categoryService.findAllCategoriesByActive(active);
    }



    /**
     * Handles an HTTP POST request to search for categories based on specified filters.
     *
     * @param filters A map of key-value pairs representing filters for category search.
     * @return A ResponseEntity with a list of CategoryDto representing categories matching the filters
     *         and an appropriate HTTP status code.
     */
    @PostMapping("/search")
    public ResponseEntity<List<CategoryDto>> findByFilters(@RequestBody Map<String, String> filters) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            List<CategoryDto> filteredCategories = categoryService.findCategoriesByFilter(filters);
            return ResponseEntity.ok(filteredCategories);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
