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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
}
