package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.CategoryDto;
import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Entity(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category implements ConvertableToDto<CategoryDto>, Cloneable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank
    @Length(min = 2, max = 40)
    private String title;

    @NotBlank
    @Length(min = 2, max = 200)
    private String description;


    @NotBlank
    @Length(min = 2, max = 100)
    private String imgUrl;

    @NotNull
    private Boolean active;

    @Override
    public CategoryDto convertToDto () {
        return new CategoryDto(id, title, description, imgUrl, active);
    }
}
