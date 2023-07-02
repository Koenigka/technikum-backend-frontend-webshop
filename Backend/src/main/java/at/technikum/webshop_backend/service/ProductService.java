package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;



    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }



    public Product createProduct(ProductDto productDto) {
        Product product = new Product();

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setImg(productDto.getImg());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setActive(productDto.getActive());

        Category category = categoryService.findById(productDto.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productDto.getId()));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setImg(productDto.getImg());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setActive(productDto.getActive());

        Category category = categoryService.findById(productDto.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }


    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);
    }


    //methods old

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

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findByTitleContains(String title){
        return productRepository.findByTitleContains(title);
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Product save(Product product, Long categoryId){
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()){
            throw new EntityNotFoundException();
        }
        product.setCategory(category.get());
        return save(product);
    }








}





