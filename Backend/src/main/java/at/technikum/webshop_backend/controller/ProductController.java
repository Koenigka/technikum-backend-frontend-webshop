package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
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

    @GetMapping("/{id}")
    public List<Product> findProductsById(@PathVariable Long id){
        return repo.findAllById(id);
    }



    @GetMapping("/byCategory/{categoryId}")
    public List<Product> findByCategory_id(@PathVariable int categoryId){
        return repo.findByCategoryId(categoryId);
    }





    @GetMapping("/searchproduct/{title}")
    public List<Product> findProductsByName(@PathVariable String title) {

        return repo.findByTitleContains(title);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){

        product = repo.save(product);
        return ResponseEntity.created(URI.create("http://localhost:8080/products")).body(product);
    }


    /*
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
