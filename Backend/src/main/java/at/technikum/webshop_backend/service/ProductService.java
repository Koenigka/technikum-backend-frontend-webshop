package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.repository.CategoryRepository;
import at.technikum.webshop_backend.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;



    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
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



    public List<ProductDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return convertToProductDtoList(products);
    }

    public List<ProductDto> findAllProductsByActive(Boolean active) {
        List<Product> products = productRepository.findByActive(active);
        return convertToProductDtoList(products);
    }

    public Optional<ProductDto> findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(Product::convertToDto);
    }

    public List<ProductDto> findProductsByFilters(Map<String, String> filters) {
        String productTitle = filters.get("filter[productTitle]");
        String categoryId = filters.get("filter[categoryId]");
        String active = filters.get("filter[active]");

        List<Product> products;

        if (productTitle != null && categoryId != null) {
            Category category = categoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
            if (active != null) {
                products = productRepository.findByTitleContainsAndCategoryAndActive(productTitle, category, Boolean.parseBoolean(active));
            } else {
                products = productRepository.findByTitleContainsAndCategory(productTitle, category);
            }
        } else if (productTitle != null) {
            if (active != null) {
                products = productRepository.findByTitleContainsAndActive(productTitle, Boolean.parseBoolean(active));
            } else {
                products = productRepository.findByTitleContains(productTitle);
            }
        } else if (categoryId != null) {
            Category category = categoryRepository.findById(Long.parseLong(categoryId)).orElse(null);
            if (active != null) {
                products = productRepository.findByCategoryAndActive(category, Boolean.parseBoolean(active));
            } else {
                products = productRepository.findByCategory(category);
            }
        } else if (active != null) {
            products = productRepository.findByActive(Boolean.parseBoolean(active));
        } else {
            products = productRepository.findAll();
        }
        return convertToProductDtoList(products);
    }


        private List<ProductDto> convertToProductDtoList(List<Product> products) {
        return products.stream()
                .map(Product::convertToDto)
                .collect(Collectors.toList());
    }


    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}





