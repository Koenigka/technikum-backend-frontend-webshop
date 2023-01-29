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
        categories.add(new Category(1L, "Cookies", "Some quick example text to build on the card title and make up the bulk of the card's content dark & white chocolate", COOKIE_URL, 1));
        categories.add(new Category(2L, "Cupcakes", "Some quick example text to build on the card title and make up the bulk of the card's content with berries", CUPCAKE_URL, 1));
        categories.add(new Category(3L, "Brownies", "Some quick example text to build on the card title and make up the bulk of the card's content dark chocolate", BROWNIE_URL,  1));
        categories.add(new Category(4L, "Macarons", "Some quick example text to build on the card title and make up the bulk of the card's content strawberry", MACARONES_URL, 1));

    }

    private static final List<Category> categories = new ArrayList<>();


    @Override
    public List<Category> findAll() {
        return categories;
    }




}
