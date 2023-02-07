package at.technikum.webshop_backend.model;

import jakarta.persistence.*;

@Entity(name = "cart")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
    private Long id;

    public Cart() {
    }

    public Cart(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
