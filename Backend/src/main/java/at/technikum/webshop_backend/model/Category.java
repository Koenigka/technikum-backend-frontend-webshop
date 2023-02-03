package at.technikum.webshop_backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity(name = "category")
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    //@OneToMany(mappedBy = "category")
    //private Set<Product> products;

    private String title;


    private String description;


    private String imgUrl;

    private Boolean active;

    public Category() {
    }

    public Category(long id, String title, String description, String imgUrl, Boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.active = active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
