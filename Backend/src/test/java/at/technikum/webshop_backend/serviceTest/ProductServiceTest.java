package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import at.technikum.webshop_backend.service.CategoryService;
import at.technikum.webshop_backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateProduct() {
        // Erstelle ein Beispiel-ProductDto
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Test Product");
        productDto.setDescription("Description");
        productDto.setImg("image.jpg");
        productDto.setPrice(10.0);
        productDto.setStock(100);
        productDto.setActive(true);
        productDto.setCategoryId(1L);

        // Erstelle ein Beispiel-Product
        Product product = new Product();
        product.setTitle("Test Product");
        product.setDescription("Description");
        product.setImg("image.jpg");
        product.setPrice(10.0);
        product.setStock(100);
        product.setActive(true);

        // Mock das erwartete Verhalten von categoryService.findById
        Category category = new Category();
        category.setId(1L); // Setzen Sie die ID entsprechend Ihrer Anwendung
        when(categoryService.findById(1L)).thenReturn(category);

        // Mock das erwartete Verhalten von productRepository.save
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Rufe die Methode createProduct auf
        Product resultProduct = productService.createProduct(productDto);

        // Überprüfe das Ergebnis
        assertNotNull(resultProduct);
        assertEquals("Test Product", resultProduct.getTitle());
        assertTrue(resultProduct.getActive());
    }

    @Test
    void testUpdateProduct() {
        // Erstelle ein Beispiel-ProductDto
        ProductDto productDto = new ProductDto();
        productDto.setId(1L); // ID eines vorhandenen Produkts
        productDto.setTitle("Updated Product");

        // Mock das erwartete Verhalten von productRepository.findById
        Product existingProduct = new Product();
        existingProduct.setId(1L); // Setzen Sie die ID entsprechend Ihrer Anwendung
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        // Mock das erwartete Verhalten von categoryService.findById
        Category category = new Category();
        category.setId(1L); // Setzen Sie die ID entsprechend Ihrer Anwendung
        when(categoryService.findById(1L)).thenReturn(category);

        // Mock das erwartete Verhalten von productRepository.save
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Rufe die Methode updateProduct auf
        Product resultProduct = productService.updateProduct(productDto);

        // Überprüfe das Ergebnis
        assertNotNull(resultProduct);
        assertEquals("Updated Product", resultProduct.getTitle());
    }

    @Test
    void testDeleteById() {
        // Mock das erwartete Verhalten von productRepository.findById
        Product existingProduct = new Product();
        existingProduct.setId(1L); // Setzen Sie die ID entsprechend Ihrer Anwendung
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        // Rufe die Methode deleteById auf
        assertDoesNotThrow(() -> productService.deleteById(1L));
    }
}
