package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


/**
 * The {@code ProductController} class handles HTTP requests related to product operations.
 * It exposes endpoints for creating products.
 *
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService){
        this.productService = productService;
    }

    private static final String authorityAdmin = "ROLE_ADMIN";

    /**
     * Handles an HTTP POST request to create a new product.
     *
     * @param productDto     The ProductDto containing the product information.
     * @param bindingResult  The BindingResult for validating the productDto.
     * @return A ResponseEntity with the created ProductDto and an appropriate HTTP status code,
     *         or a forbidden status if the requester is not an admin, or a bad request status
     *         with validation errors if the productDto is invalid.
     */
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

    /**
     * Handles an HTTP PUT request to update a product.
     *
     * @param productDto    The ProductDto containing the updated product information.
     * @param bindingResult The BindingResult for validating the productDto.
     * @return A ResponseEntity with the updated ProductDto and an appropriate HTTP status code,
     *         or a bad request status with validation errors if the productDto is invalid,
     *         or a forbidden status if the requester is not an admin.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateProduct (@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {


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

    /**
     * Handles an HTTP DELETE request to delete a product by its ID.
     *
     * @param id The ID of the product to be deleted.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK if the product was successfully deleted.
     *         - 404 Not Found if the product with the specified ID does not exist.
     *         - 403 Forbidden if the requester does not have admin privileges.
     */
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


    /**
     * Handles an HTTP GET request to retrieve a list of all products.
     *
     * @return A list of ProductDto objects representing all products.
     */
    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAllProducts();
    }

    /**
     * Handles an HTTP GET request to retrieve a list of products filtered by their active status.
     *
     * @param active The active status to filter the products. True for active, false for inactive.
     * @return A list of ProductDto objects representing products that match the given active status.
     */
    @GetMapping("/isActive/{active}")
    public List<ProductDto> findAllProductsByActive(@PathVariable Boolean active) {
        return productService.findAllProductsByActive(active);
    }

    /**
     * Handles an HTTP GET request to retrieve a product by its ID.
     *
     * @param id The ID of the product to be retrieved.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with the ProductDto if the product is found.
     *         - 404 Not Found if the product with the specified ID does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        Optional<ProductDto> productDto = productService.findProductById(id);
        return productDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    /**
     * Handles an HTTP POST request to search for products based on specified filters.
     *
     * @param filters A Map containing key-value pairs representing filters for product search.
     *                The keys are filter criteria, and the values are the corresponding filter values.
     * @return A ResponseEntity representing the HTTP response:
     *         - 200 OK with a list of ProductDto objects matching the specified filters if the requester is an admin.
     *         - 403 Forbidden if the requester does not have admin privileges.
     */
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

    /**
     * Handles an HTTP GET request to retrieve a list of products by their category ID and active status.
     *
     * @param categoryId The ID of the category to filter the products by.
     * @param active     The active status to filter the products. True for active, false for inactive.
     * @return A list of ProductDto objects representing products that match the given category ID and active status.
     */
    @GetMapping("/byCategory/{categoryId}/{active}")
    public List<ProductDto> findByCategoryIdAndActive(@PathVariable Long categoryId, @PathVariable Boolean active){
        return productService.findByCategoryIdAndActive(categoryId, active);
    }

}
