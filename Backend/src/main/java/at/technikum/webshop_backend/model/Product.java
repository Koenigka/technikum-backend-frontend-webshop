package at.technikum.webshop_backend.model;

public class Product {

    private Long id;

    private String title;

    private String description;

    private String img;

    private double price;

    private int stock;
    private int category_id;

    private int isActive;

    public Product() {
    }

    public Product(Long id, String title, String description, String img, double price, int stock, int category_id, int isActive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.img = img;
        this.price = price;
        this.stock = stock;
        this.category_id = category_id;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
