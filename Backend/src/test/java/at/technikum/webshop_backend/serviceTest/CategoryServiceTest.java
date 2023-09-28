package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateCategory() {
        // Erstelle ein Beispiel-CategoryDto
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setTitle("Test Category");
        categoryDto.setDescription("Description");
        categoryDto.setImgUrl("image.jpg");
        categoryDto.setActive(true);

        // Erstelle ein Beispiel-Category
        Category category = new Category();
        category.setTitle("Test Category");
        category.setDescription("Description");
        category.setImgUrl("image.jpg");
        category.setActive(true);

        // Mock das erwartete Verhalten des categoryRepository
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Rufe die Methode createCategory auf
        Category resultCategory = categoryService.createCategory(categoryDto);

        // Überprüfe, ob categoryRepository.save() einmal aufgerufen wurde
        verify(categoryRepository, times(1)).save(any(Category.class));

        // Überprüfe das Ergebnis
        assertNotNull(resultCategory);
        assertEquals("Test Category", resultCategory.getTitle());

    }
}
