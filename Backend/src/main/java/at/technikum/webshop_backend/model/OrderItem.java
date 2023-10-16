package at.technikum.webshop_backend.model;


import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.dto.OrderItemDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "orderItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements ConvertableToDto<OrderItemDto>, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customerOrder_id", nullable = false)
    @JsonIgnore
    private CustomerOrder customerOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private Double price;

    public OrderItemDto convertToDto() {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(id);
        orderItemDto.setCustomerOrderId(customerOrder.getId());
        orderItemDto.setQuantity(quantity);
        orderItemDto.setPrice(price);
        orderItemDto.setProductId(product.getId());
        orderItemDto.setTitle(product.getTitle());
        orderItemDto.setDescription(product.getDescription());
        orderItemDto.setImg(product.getImg());
        return orderItemDto;
    }
}