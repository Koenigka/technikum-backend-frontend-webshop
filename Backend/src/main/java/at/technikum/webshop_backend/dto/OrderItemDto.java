package at.technikum.webshop_backend.dto;

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
public class OrderItemDto implements DataTransferObject {

    private Long id;

    private Long customerOrderId;

    private int quantity;

    private double price;

    private Long productId;
    private String title;

    private String description;

    private String img;



}
