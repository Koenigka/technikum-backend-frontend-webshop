package at.technikum.webshop_backend.dto;


import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.utils.DataTransferObject;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link Product}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements DataTransferObject {


    private Long id;

    @NotBlank(message = "The 'title' field must not be blank.")
    @Size(max = 255, message = "The 'title' field must not exceed 255 characters.")
    private String title;

    @NotBlank(message = "The 'description' field must not be blank.")
    @Size(max = 1000, message = "The 'description' field must not exceed 1005 characters.")
    private String description;

    @NotBlank(message = "The 'img' field must not be blank.")
    @Size(max = 1000, message = "The 'img' field must not exceed 1000 characters.")
    private String img;

    @DecimalMin(value = "0.01", message = "The price must not be negative.")
    private double price;

    @PositiveOrZero(message = "The 'stock' field must not be negative.")
    private int stock;

    @NotNull(message = "The 'active' field must not be null.")
    private Boolean active;

    @NotNull(message = "The 'categoryId' field must not be null.")
    private Long categoryId;

}
