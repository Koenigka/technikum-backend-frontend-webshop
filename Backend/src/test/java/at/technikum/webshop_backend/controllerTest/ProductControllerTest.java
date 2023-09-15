package at.technikum.webshop_backend.controllerTest;

import at.technikum.webshop_backend.controller.ProductController;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Mock
    private ProductService productService;

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

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/products/create")
                        .content(productDtoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testFindAllProductsByActive() throws Exception {
        boolean active = true;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/products/isActive/{active}", active)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
