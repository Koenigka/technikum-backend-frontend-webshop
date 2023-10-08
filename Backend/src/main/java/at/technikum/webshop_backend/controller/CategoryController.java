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

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private static final String authorityAdmin = "ADMIN";

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


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

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        CategoryDto categoryDto = category.convertToDto();
        return ResponseEntity.ok(categoryDto);
    }
    @GetMapping("/isActive/{active}")
    public List<CategoryDto> findAllCategoriesByActive(@PathVariable Boolean active) {
        return categoryService.findAllCategoriesByActive(active);
    }
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
