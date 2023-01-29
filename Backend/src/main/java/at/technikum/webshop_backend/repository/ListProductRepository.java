package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ListProductRepository implements ProductRepository{

private static final String COOKIE_URL = "img/white_dark_cookie_1920.jpg";
private static final String CUPCAKE_URL = "img/cupcake_1920.jpg";

private static final String BROWNIE_URL = "img/browniex_1920.jpg";

private static final String MACARONES_URL = "img/macarons-5264197_1920.jpg";

    {
        products.add(new Product(1L, "Cookie", "dark & white chocolate", COOKIE_URL, 4.99F, 50, 1, 1));
        products.add(new Product(2L, "Cupcake", "with berries", CUPCAKE_URL, 5.99F, 50, 2,1));
        products.add(new Product(3L, "Brownie", "dark chocolate", BROWNIE_URL, 6.99F, 50, 3,1));
        products.add(new Product(4L, "Macaron", "strawberry", MACARONES_URL, 10.99F, 50, 4,1));
        products.add(new Product(5L, "Macaron", "strawberry", MACARONES_URL, 10.99F, 50, 4,1));

    }

    private static final List<Product> products = new ArrayList<>();


    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public List<Product> findAllById(int category_id) {
        // return products.stream().filter(p -> p.getType().contains(type)).toList();

        List<Product> matches = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory_id() == (category_id)) {
                matches.add(p);
            }
        }

        return matches;
    }



}
