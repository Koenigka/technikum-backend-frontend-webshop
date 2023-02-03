package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;



    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    //METHODS

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByActive(Boolean active){return productRepository.findByActive(active);}
    public List<Product> findByCategoryId(Long categoryId){
        return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> findByCategoryIdAndActive(Long categoryId, Boolean active){
        return productRepository.findByCategoryIdAndActive(categoryId, active);

    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Product> findByTitleContains(String title){
        return productRepository.findByTitleContains(title);
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product save(Product product, Long categoryId){
        var category = categoryRepository.findById(categoryId);

        if(category.isEmpty()){
            throw new EntityNotFoundException();
        }
        product.setCategory(category.get());
        return save(product);
    }


    public Product update(Long id, Product updatedProduct){
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setTitle(updatedProduct.getTitle());
        product.setDescription(updatedProduct.getDescription());
        product.setImg(updatedProduct.getImg());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setActive(updatedProduct.getActive());
        product.setCategory(updatedProduct.getCategory());

        return productRepository.save(product);
    }

    public void deleteById(Long id){
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        productRepository.deleteById(id);
    }



}





