package at.technikum.webshop_backend.model;

public class Product {

    private Long id;

    private String title;

    private String description;

    private long fid_category;

    private double price;

    private int stock;

    public Product(Long id, String title, String description, long fid_category, double price, int stock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fid_category = fid_category;
        this.price = price;
        this.stock = stock;
    }

    public Product() {
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

    public long getFid_category() {
        return fid_category;
    }

    public void setFid_category(long fid_category) {
        this.fid_category = fid_category;
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
}
