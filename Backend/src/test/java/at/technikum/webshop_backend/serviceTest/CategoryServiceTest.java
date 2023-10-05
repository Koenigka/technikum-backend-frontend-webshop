package at.technikum.webshop_backend.serviceTest;


import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import at.technikum.webshop_backend.service.CategoryService;
import at.technikum.webshop_backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    public void testCreateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setTitle("Test Category");
        categoryDto.setDescription("Category Description");
        categoryDto.setImgUrl("img-url");
        categoryDto.setActive(true);

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setTitle("Test Category");
        mockCategory.setDescription("Category Description");
        mockCategory.setImgUrl("img-url");
        mockCategory.setActive(true);

        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        Category createdCategory = categoryService.createCategory(categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));

        assertEquals("Test Category", createdCategory.getTitle());
        assertEquals("Category Description", createdCategory.getDescription());
        assertEquals("img-url", createdCategory.getImgUrl());
        assertEquals(true, createdCategory.getActive());
    }


    @Test
    public void testFindAllCategoriesByActive() {
        Boolean active = true;

        List<Category> dummyCategories = Arrays.asList(
                new Category(1L, "Category 1", "Description 1", "img-url-1", true),
                new Category(2L, "Category 2", "Description 2", "img-url-2", true)

        );

        when(categoryRepository.findAllByActive(active)).thenReturn(dummyCategories);

        List<CategoryDto> categories = categoryService.findAllCategoriesByActive(active);

        assertEquals(2, categories.size());

        assertEquals("Category 1", categories.get(0).getTitle());
        assertEquals("Category 2", categories.get(1).getTitle());

        verify(categoryRepository, times(1)).findAllByActive(active);
    }

}
