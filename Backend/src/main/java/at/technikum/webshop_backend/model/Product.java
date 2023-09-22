package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements ConvertableToDto<ProductDto>, Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    private String description;

    private String img;

    private double price;

    private int stock;

    private Boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Override
    public ProductDto convertToDto () {
        return new ProductDto(id, title, description, img, price, stock, active, category.getId());
    }

}