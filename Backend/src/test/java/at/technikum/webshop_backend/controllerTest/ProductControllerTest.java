package at.technikum.webshop_backend.controllerTest;

import at.technikum.webshop_backend.controller.ProductController;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
    }

    @Test
    public void testCreateProductAsNonAdmin() {
        ProductDto inputProductDto = new ProductDto();

        List<GrantedAuthority> authorities = Collections.singletonList(() -> "USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<ProductDto> response = productController.createProduct(inputProductDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testFindAllProductsByActive() {
        boolean active = true;
        List<ProductDto> expectedProductList = new ArrayList<>();
        expectedProductList.add(new ProductDto());
        expectedProductList.add(new ProductDto());

        when(productService.findAllProductsByActive(active)).thenReturn(expectedProductList);

        List<ProductDto> response = productController.findAllProductsByActive(active);

        assertEquals(expectedProductList, response);

        verify(productService, times(1)).findAllProductsByActive(active);
    }

}
