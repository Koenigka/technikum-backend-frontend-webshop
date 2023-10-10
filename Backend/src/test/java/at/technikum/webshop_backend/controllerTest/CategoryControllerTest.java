package at.technikum.webshop_backend.controllerTest;


import at.technikum.webshop_backend.controller.CategoryController;
import at.technikum.webshop_backend.controller.ProductController;
import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryController(categoryService)).build();
    }

    @Test
    public void testFindAllCategoriesByActive() throws Exception {
        Boolean active = true;

        List<CategoryDto> categories = Arrays.asList(
                new CategoryDto(1L, "Category 1", "Description 1", "img-url-1", true),
                new CategoryDto(2L, "Category 2", "Description 2", "img-url-2", true)
        );
        when(categoryService.findAllCategoriesByActive(active)).thenReturn(categories);

        mockMvc.perform(get("/api/categories/isActive/{active}", active)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Category 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].imgUrl").value("img-url-1"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Category 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].imgUrl").value("img-url-2"))
                .andExpect(jsonPath("$[1].active").value(true));

        verify(categoryService, times(1)).findAllCategoriesByActive(active);
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testFindByFiltersAsAdmin() throws Exception {
        Map<String, String> filters = new HashMap<>();
        filters.put("filterKey", "filterValue");

        List<CategoryDto> categories = Arrays.asList(
                new CategoryDto(1L, "Category 1", "Description 1", "img-url-1", true),
                new CategoryDto(2L, "Category 2", "Description 2", "img-url-2", true)
        );
        when(categoryService.findCategoriesByFilter(filters)).thenReturn(categories);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/categories/search")
                        .content(objectMapper.writeValueAsString(filters)) // JSON-Konvertierung hier
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Category 1"))
                .andExpect(jsonPath("$[1].title").value("Category 2"));

        verify(categoryService, times(1)).findCategoriesByFilter(filters);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testFindByFiltersAsNonAdmin() throws Exception {
        Map<String, String> filters = new HashMap<>();
        filters.put("filterKey", "filterValue");

        when(categoryService.findCategoriesByFilter(filters)).thenReturn(null);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/categories/search")
                        .content(objectMapper.writeValueAsString(filters))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(categoryService, never()).findCategoriesByFilter(filters);
    }



}
