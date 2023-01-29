package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.ListProductRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repo = new ListProductRepository();


    @GetMapping
    public List<Product> findAllProducts() {
        return repo.findAll();
    }

    @GetMapping("/{category_id}")
    public List<Product> findAllProductsByType(@PathVariable int category_id) {
        return repo.findAllById(category_id);
    }

    /*
    @PostMapping("")
    public Product create(@RequestBody Product product){

        this.products.add(product);
        return product;
    }

    @GetMapping("")
    public List<Product> readAll(){

        return this.products;
    }


    @GetMapping ("/{id}")
    public Product read(@PathVariable Long id){

        for (Product product : this.products) {
            if(id.equals( product.getId())){
                return product;
            }
        }
        //404 question not found
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public Product update(){
        return null;
    }

    @DeleteMapping("/{id}")
    public Product delete(){
        return null;
    }*/
}

