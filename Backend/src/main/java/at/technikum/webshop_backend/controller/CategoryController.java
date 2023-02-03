package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository repo;

    @GetMapping
    public List<Category> findAllCategories(){
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        category = repo.save(category);
        return ResponseEntity.created(URI.create("http://localhost:8080/categories")).body(category);
    }
}
