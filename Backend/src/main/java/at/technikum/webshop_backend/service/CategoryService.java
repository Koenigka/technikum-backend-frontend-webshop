package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    //INIT
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //Methods


    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();

    }

    public List<Category> findAllByActive(Boolean active){
        return categoryRepository.findAllByActive(active);
    }

    public Category update(Long id, Category updatedCategory){
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        category.setTitle(updatedCategory.getTitle());
        category.setDescription(updatedCategory.getDescription());
        category.setImgUrl(updatedCategory.getImgUrl());
        category.setActive(updatedCategory.getActive());

        return categoryRepository.save(category);

    }

    public void deleteById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        categoryRepository.deleteById(id);
    }


}
