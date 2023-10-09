package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.utils.DataTransferObject;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto implements DataTransferObject {

    private Long id;

    @NotNull(message = "The 'userId' field must not be null.")
    private Long userId;

    @NotNull(message = "The 'productId' field must not be null.")
    private Long productId;

    @PositiveOrZero(message = "The 'quantity' field must be a positive number or zero.")
    private int quantity;
    private LocalDateTime creationDate;

    @Size(max = 255)
    private String title;

    @Length(max = 1000)
    private String description;

    @Length(max = 1000)
    private String img;

    @DecimalMin(value = "0.01")
    private double price;

}
