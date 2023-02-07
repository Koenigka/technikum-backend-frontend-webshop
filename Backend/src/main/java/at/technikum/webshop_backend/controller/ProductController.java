package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.ProductDTO;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.ProductRepository;
import at.technikum.webshop_backend.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product createProduct(@Valid @RequestBody ProductDTO productDTO){
        return productService.save(fromDTO(productDTO), productDTO.getCategoryId());
    }

    private static Product fromDTO(ProductDTO productDTO){
        return new Product(productDTO.getId(), productDTO.getTitle(), productDTO.getDescription(),
                productDTO.getImg(), productDTO.getPrice(), productDTO.getStock(),productDTO.getActive());
    }



    @GetMapping()
    public List<Product> findAll(){
        return productService.findAll();
    }
    @GetMapping("/isActive/{active}")
    public List<Product> findAllProductsByActive(@PathVariable Boolean active){
        return productService.findByActive(active);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id){
        return productService.findById(id);
    }


    @GetMapping("/byCategory/{categoryId}/{active}")
    public List<Product> findByCategoryIdAndActive(@PathVariable Long categoryId, @PathVariable Boolean active){
        return productService.findByCategoryIdAndActive(categoryId, active);
    }

    @GetMapping("/searchproduct/{title}")
    public List<Product> findProductsByTitle(@PathVariable String title) {

        return productService.findByTitleContains(title);
    }


    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody @Valid Product product){
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void  deleteProduct(@PathVariable Long id){
        productService.deleteById(id);
    }


}
