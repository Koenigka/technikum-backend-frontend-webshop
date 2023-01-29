package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ListCategoryRepository;
import at.technikum.webshop_backend.repository.ListProductRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryRepository repo = new ListCategoryRepository();


    @GetMapping
    public List<Category> findAllCategories() {
        return repo.findAll();
    }


}
