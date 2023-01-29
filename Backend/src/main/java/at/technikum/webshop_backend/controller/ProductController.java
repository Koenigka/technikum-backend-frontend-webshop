package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @GetMapping
    public List<Product> findAllProducts(){
        return repo.findAll();
    }

    @GetMapping("/{category_id}")
    public List<Product> findAllProductsByCategory(@PathVariable int category_id){
        return repo.findAllById(category_id);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){

        product = repo.save(product);
        return ResponseEntity.created(URI.create("http://localhost:8080/products")).body(product);
    }

}
