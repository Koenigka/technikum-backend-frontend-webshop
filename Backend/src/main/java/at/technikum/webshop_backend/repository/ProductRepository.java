package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    List<Product> findAllByType(String type);
}
