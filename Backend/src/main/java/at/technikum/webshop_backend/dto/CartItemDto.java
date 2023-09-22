package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.utils.DataTransferObject;
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
    private Long userId;
    private Long productId;
    private int quantity;
    private LocalDateTime creationDate;
}
