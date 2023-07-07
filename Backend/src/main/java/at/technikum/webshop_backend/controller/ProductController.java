package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.security.CustomUserDetailService;
import at.technikum.webshop_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct (@RequestBody ProductDto productDto) {

        //get authenticated User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Check if has Role_Admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            Product createdProduct = productService.createProduct(productDto);
            ProductDto newProduct = createdProduct.convertToDto();
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct (@RequestBody ProductDto productDto) {
        //get authenticated User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Check if has Role_Admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin){
            Product updatedProduct = productService.updateProduct(productDto);
        ProductDto updatedProductDto = updatedProduct.convertToDto();
        return ResponseEntity.ok(updatedProductDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct (@PathVariable Long id) {
        //get authenticated User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Check if has Role_Admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            try {
                productService.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    //TODO alles auf Response Entity ProductDto ändern
    @GetMapping()
    public List<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/isActive/{active}")
    public List<Product> findAllProductsByActive(@PathVariable Boolean active){
        return productService.findByActive(active);
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id){
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



}
