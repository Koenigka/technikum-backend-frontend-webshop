package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService){
        this.productService = productService;
    }

    private static final String authorityAdmin = "ADMIN";

    @PostMapping("/create")
    public ResponseEntity<?> createProduct (@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : fieldErrors) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        Product createdProduct = productService.createProduct(productDto);
        ProductDto newProduct = createdProduct.convertToDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);


    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct (@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : fieldErrors) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }


        Product updatedProduct = productService.updateProduct(productDto);
        ProductDto updatedProductDto = updatedProduct.convertToDto();
        return ResponseEntity.ok(updatedProductDto);

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

    @PostMapping("/search")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestBody Map<String, String> filters) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (isAdmin) {
            List<ProductDto> filteredProducts = productService.findProductsByFilters(filters);
            return ResponseEntity.ok(filteredProducts);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/byCategory/{categoryId}/{active}")
    public List<ProductDto> findByCategoryIdAndActive(@PathVariable Long categoryId, @PathVariable Boolean active){
        return productService.findByCategoryIdAndActive(categoryId, active);
    }

}
