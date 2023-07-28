package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final String authorityAdmin = "ADMIN";

    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct (@RequestBody ProductDto productDto) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if(isAdmin) {

            Product createdProduct = productService.createProduct(productDto);
            ProductDto newProduct = createdProduct.convertToDto();
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct (@RequestBody ProductDto productDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if(isAdmin) {
            Product updatedProduct = productService.updateProduct(productDto);
        ProductDto updatedProductDto = updatedProduct.convertToDto();
        return ResponseEntity.ok(updatedProductDto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct (@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if(isAdmin) {
            try {
                productService.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }


    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAllProducts();
    }

    @GetMapping("/isActive/{active}")
    public List<ProductDto> findAllProductsByActive(@PathVariable Boolean active) {
        return productService.findAllProductsByActive(active);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        Optional<ProductDto> productDto = productService.findProductById(id);
        return productDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byCategory/{categoryId}/{active}")
    public List<ProductDto> findByCategoryIdAndActive(@PathVariable Long categoryId, @PathVariable Boolean active) {
        return productService.findProductsByCategoryIdAndActive(categoryId, active);
    }

    @GetMapping("/searchproduct/{title}")
    public List<ProductDto> findProductsByTitle(@PathVariable String title) {
        return productService.findProductsByTitleContains(title);
    }



}
