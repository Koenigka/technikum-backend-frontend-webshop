package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Length(max = 1000)
    private String description;

    @NotBlank
    @Length(max = 1000)
    private String img;

    @DecimalMin(value = "0.01")
    private double price;

    @PositiveOrZero
    private int stock;

    @NotNull
    private Boolean active;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Override
    public ProductDto convertToDto () {
        return new ProductDto(id, title, description, img, price, stock, active, category.getId());
    }

}