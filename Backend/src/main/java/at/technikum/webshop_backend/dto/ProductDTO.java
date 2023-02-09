package at.technikum.webshop_backend.dto;


import at.technikum.webshop_backend.model.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link Product}
 */

public class ProductDTO {


    private Long id;

    @NotBlank
    @Length(min = 2, max = 40)
    private String title;

    @NotBlank
    @Length(min = 2, max = 200)
    private String description;

    @NotBlank
    @Length(min = 2, max = 100)
    private String img;

    @NotNull
    @DecimalMin("0.01")
    private double price;

    @NotNull
    private int stock;
    @NotNull
    private Boolean active;
    @NotNull
    private Long categoryId;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
