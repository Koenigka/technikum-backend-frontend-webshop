package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category){
        return categoryService.save(category);
    }


    @GetMapping
    public List<Category> findAll(){
        return categoryService.findAll();
    }
    @GetMapping("/{active}")
    public List<Category> findAllCategoriesByActive(@PathVariable Boolean active){
        return categoryService.findAllByActive(active);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category){
        return categoryService.update(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteById(id);

    }

}
