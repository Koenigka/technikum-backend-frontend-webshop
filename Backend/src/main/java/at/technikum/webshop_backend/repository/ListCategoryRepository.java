package at.technikum.webshop_backend.repository;

import at.technikum.webshop_backend.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryRepository implements CategoryRepository {


    private static final String COOKIE_URL = "img/white_dark_cookie_1920.jpg";
    private static final String CUPCAKE_URL = "img/cupcake_1920.jpg";

    private static final String BROWNIE_URL = "img/browniex_1920.jpg";

    private static final String MACARONES_URL = "img/macarons-5264197_1920.jpg";

    {
        categories.add(new Category(1L, "Cookie", "Some quick example text to build on the card title and make up the bulk of the card's content dark & white chocolate", COOKIE_URL, "Cookie"));
        categories.add(new Category(2L, "Cupcake", "Some quick example text to build on the card title and make up the bulk of the card's content with berries", CUPCAKE_URL, "Cupcake"));
        categories.add(new Category(3L, "Brownie", "Some quick example text to build on the card title and make up the bulk of the card's content dark chocolate", BROWNIE_URL,  "Brownie"));
        categories.add(new Category(4L, "Macaron", "Some quick example text to build on the card title and make up the bulk of the card's content strawberry", MACARONES_URL, "Macaron"));

    }

    private static final List<Category> categories = new ArrayList<>();


    @Override
    public List<Category> findAll() {
        return categories;
    }

    @Override
    public List<Category> findAllByType(String type) {
        // return products.stream().filter(p -> p.getType().contains(type)).toList();

        List<Category> matches = new ArrayList<>();
        for (Category c : categories) {
            if (c.getType().contains(type)) {
                matches.add(c);
            }
        }

        return matches;
    }


}
