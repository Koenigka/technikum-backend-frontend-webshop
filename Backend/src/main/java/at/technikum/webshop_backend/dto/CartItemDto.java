package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.utils.DataTransferObject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
