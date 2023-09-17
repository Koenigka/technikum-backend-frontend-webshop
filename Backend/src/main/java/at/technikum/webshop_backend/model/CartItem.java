    package at.technikum.webshop_backend.model;

    import at.technikum.webshop_backend.dto.ProductDto;
    import at.technikum.webshop_backend.utils.ConvertableToDto;
    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.HashSet;
    import java.util.Set;

    @Entity(name = "cart")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Cart {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long Id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = true)
        private User user;

        @OneToMany(mappedBy = "cart",  cascade = CascadeType.ALL)
        private Set<Position> positions = new HashSet<>();

        @Column(name = "cart_status")
        @Enumerated(EnumType.STRING)
        private String status;

        public Cart(User user) {
            this.user = user;
        }
    }
