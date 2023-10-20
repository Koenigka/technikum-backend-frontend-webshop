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

/**
 * Service class for managing products, including creation, updating, deletion, and retrieval operations.
 */
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

    /**
     * Saves a product to the database.
     *
     * @param product The product to be saved.
     */
    public void save(Product product) {
        productRepository.save(product);
    }


    /**
     * Creates a new product from a ProductDto.
     *
     * @param productDto The ProductDto containing product information.
     * @return The created Product.
     */
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

    /**
     * Updates an existing product using information from a ProductDto.
     *
     * @param productDto The ProductDto containing updated product information.
     * @return The updated Product.
     * @throws EntityNotFoundException if the product to update is not found.
     */
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

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @throws EntityNotFoundException if the product to delete is not found.
     */
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);
    }


    /**
     * Retrieves a list of all products in the database.
     *
     * @return A list of ProductDto objects representing all products.
     */
    public List<ProductDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return convertToProductDtoList(products);
    }

    /**
     * Retrieves a list of products based on their active status.
     *
     * @param active The active status to filter products by.
     * @return A list of ProductDto objects matching the active status.
     */
    public List<ProductDto> findAllProductsByActive(Boolean active) {
        List<Product> products = productRepository.findByActive(active);
        return convertToProductDtoList(products);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An optional containing the ProductDto if found, or empty if not found.
     */
    public Optional<ProductDto> findProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(Product::convertToDto);
    }

    /**
     * Retrieves a list of products based on specified filters.
     *
     * @param filters A map of filter criteria for querying products.
     * @return A list of ProductDto objects matching the filter criteria.
     */
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

    /**
     * Converts a list of Product objects to a list of ProductDto objects.
     *
     * @param products The list of Product objects to convert.
     * @return A list of ProductDto objects.
     */
    public List<ProductDto> convertToProductDtoList(List<Product> products) {
        return products.stream()
                .map(Product::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An optional containing the Product if found, or empty if not found.
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }


    /**
     * Retrieves a list of products by category ID and active status.
     *
     * @param categoryId The ID of the category to filter by.
     * @param active     The active status to filter products by.
     * @return A list of ProductDto objects matching the category and active status.
     */
    public List<ProductDto> findByCategoryIdAndActive(Long categoryId, Boolean active){

        List<Product> products =  productRepository.findByCategoryIdAndActive(categoryId, active);

        return convertToProductDtoList(products);

    }
}





