package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private List<Product> products;

    public ProductController(){

        this.products = new ArrayList<>();
        this.products.add(new Product(1L, "Chocolate Cookie", "wonderfull Cookie, with dark chocolate",1L, 5.99, 100));
        this.products.add(new Product(2L, "White Cookie", "wonderfull Cookie, with white chocolate",2L, 6.99, 150));

    }

    @PostMapping("")
    public Product create(Product product){

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
    }
}

