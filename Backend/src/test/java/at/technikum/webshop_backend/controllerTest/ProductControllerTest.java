package at.technikum.webshop_backend.controllerTest;

import at.technikum.webshop_backend.controller.ProductController;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.service.CategoryService;
import at.technikum.webshop_backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService)).build();
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testCreateProductAsNonAdmin() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Test Product");
        productDto.setDescription("Description");
        productDto.setPrice(10.99);


        ObjectMapper objectMapper = new ObjectMapper();
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/api/products/create")
                        .content(productDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testFindAllProductsByActive() throws Exception {
        boolean active = true;

        mockMvc.perform(get("/api/products/isActive/{active}", active)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testUpdateProductAsAdmin() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle("Updated Product");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setTitle("Updated Product");

        when(productService.updateProduct(productDto)).thenReturn(updatedProduct);

        ObjectMapper objectMapper = new ObjectMapper();
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(put("/api/products/update")
                        .content(productDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Product"));

    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testGetProductsAsNonAdmin() throws Exception {
        mockMvc.perform(post("/api/products/search")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());


    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testGetProductsAsAdmin() throws Exception {
        List<ProductDto> dummyProducts = new ArrayList<>();
        when(productService.findProductsByFilters(any(Map.class))).thenReturn(dummyProducts);

        Map<String, String> filters = new HashMap<>();
        filters.put("filterKey", "filterValue");

        String jsonFilters = new ObjectMapper().writeValueAsString(filters);

        mockMvc.perform(post("/api/products/search")
                        .content(jsonFilters)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
