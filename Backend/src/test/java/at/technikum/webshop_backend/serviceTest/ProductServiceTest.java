package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import at.technikum.webshop_backend.service.CategoryService;
import at.technikum.webshop_backend.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, categoryRepository, categoryService);
    }

    @Test
    public void testCreateProduct() {
        Category mockCategory = new Category();
        mockCategory.setId(1L);
        when(categoryService.findById(1L)).thenReturn(mockCategory);

        ProductDto productDto = new ProductDto();
        productDto.setTitle("Test Product");
        productDto.setDescription("Description");
        productDto.setImg("img-url");
        productDto.setPrice(10.0);
        productDto.setStock(50);
        productDto.setActive(true);
        productDto.setCategoryId(1L);

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setTitle("Test Product");
        mockProduct.setDescription("Description");
        mockProduct.setImg("img-url");
        mockProduct.setPrice(10.0);
        mockProduct.setStock(50);
        mockProduct.setActive(true);
        mockProduct.setCategory(mockCategory);

        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        Product createdProduct = productService.createProduct(productDto);

        verify(productRepository, times(1)).save(any(Product.class));

        assertEquals("Test Product", createdProduct.getTitle());
        assertEquals("Description", createdProduct.getDescription());
        assertEquals("img-url", createdProduct.getImg());
        assertEquals(10.0, createdProduct.getPrice());
        assertEquals(50, createdProduct.getStock());
        assertEquals(true, createdProduct.getActive());
        assertEquals(1L, createdProduct.getCategory().getId());
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Long categoryId = 1L;
        Category existingCategory = new Category();
        existingCategory.setId(categoryId);

        when(categoryService.findById(categoryId)).thenReturn(existingCategory);

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);
        productDto.setTitle("Updated Product");
        productDto.setDescription("Updated Description");
        productDto.setImg("updated-img-url");
        productDto.setPrice(20.0);
        productDto.setStock(100);
        productDto.setActive(true);
        productDto.setCategoryId(categoryId);

        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product updatedProduct = productService.updateProduct(productDto);

        verify(productRepository, times(1)).findById(productId);
        verify(categoryService, times(1)).findById(categoryId);
        verify(productRepository, times(1)).save(any(Product.class));

        assertEquals(productId, updatedProduct.getId());
        assertEquals("Updated Product", updatedProduct.getTitle());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals("updated-img-url", updatedProduct.getImg());
        assertEquals(20.0, updatedProduct.getPrice());
        assertEquals(100, updatedProduct.getStock());
        assertEquals(true, updatedProduct.getActive());
        assertEquals(categoryId, updatedProduct.getCategory().getId());
    }


    @Test
    public void testUpdateProductNotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);
        productDto.setTitle("Updated Product");
        productDto.setDescription("Updated Description");
        productDto.setImg("updated-img-url");
        productDto.setPrice(20.0);
        productDto.setStock(100);
        productDto.setActive(true);
        productDto.setCategoryId(1L);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(productDto);
        });

        assertEquals("Product not found with ID: " + productId, exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProductById() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.deleteById(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);
    }


}
