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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    public void testUpdateCategory() {
        Category existingCategory = new Category(1L, "Existing Category", "Description", "img-url", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category updatedCategory = invocation.getArgument(0);
            return updatedCategory;
        });

        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(1L);
        updatedCategoryDto.setTitle("Updated Category");
        updatedCategoryDto.setDescription("Updated Description");
        updatedCategoryDto.setImgUrl("updated-img-url");
        updatedCategoryDto.setActive(false);

        Category updatedCategory = categoryService.updateCategory(updatedCategoryDto);

        assertEquals("Updated Category", updatedCategory.getTitle());
        assertEquals("Updated Description", updatedCategory.getDescription());
        assertEquals("updated-img-url", updatedCategory.getImgUrl());
        assertFalse(updatedCategory.getActive());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    @Test
    public void testDeleteCategory() {
        Category existingCategory = new Category(1L, "Existing Category", "Description", "img-url", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(existingCategory);
    }

    @Test
    public void testFindAllCategories() {
        List<Category> dummyCategories = Arrays.asList(
                new Category(1L, "Category 1", "Description 1", "img-url-1", true),
                new Category(2L, "Category 2", "Description 2", "img-url-2", true)
        );

        when(categoryRepository.findAll()).thenReturn(dummyCategories);

        List<CategoryDto> categories = categoryService.findAllCategories();

        assertEquals(2, categories.size());
        assertEquals("Category 1", categories.get(0).getTitle());
        assertEquals("Category 2", categories.get(1).getTitle());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindCategoriesByFilter() {
        // Erstellen Sie Beispiel-Kategorien
        Category category1 = new Category(1L, "Category 1", "Description 1", "img-url-1", true);
        Category category2 = new Category(2L, "Category 2", "Description 2", "img-url-2", true);
        Category category3 = new Category(3L, "Another Category", "Description 3", "img-url-3", false);

        when(categoryRepository.findByTitleContainsAndActive("Category", true)).thenReturn(Arrays.asList(category1, category2));
        when(categoryRepository.findByTitleContainsAndActive("Another Category", false)).thenReturn(Arrays.asList(category3));

        Map<String, String> filters1 = Map.of("filter[title]", "Category", "filter[active]", "true");
        Map<String, String> filters2 = Map.of("filter[title]", "Another Category", "filter[active]", "false");

        List<CategoryDto> filteredCategories1 = categoryService.findCategoriesByFilter(filters1);
        List<CategoryDto> filteredCategories2 = categoryService.findCategoriesByFilter(filters2);

        assertEquals(2, filteredCategories1.size());
        assertEquals(1, filteredCategories2.size());
        assertEquals("Category 1", filteredCategories1.get(0).getTitle());
        assertEquals("Category 2", filteredCategories1.get(1).getTitle());
        assertEquals("Another Category", filteredCategories2.get(0).getTitle());

        verify(categoryRepository, times(1)).findByTitleContainsAndActive("Category", true);
        verify(categoryRepository, times(1)).findByTitleContainsAndActive("Another Category", false);
    }

}
