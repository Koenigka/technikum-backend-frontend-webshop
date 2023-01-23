package at.technikum.webshop_backend.model;

public class Category {

    private Long id;

    private String title;

    private String description;

    private String img;

    private String type;


    public Category() {
    }

    public Category(Long id, String title, String description, String img, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.img = img;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
