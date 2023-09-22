package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "cartItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements ConvertableToDto<CartItemDto>, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Override
    public CartItemDto convertToDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(id);
        cartItemDto.setUserId(user.getId());
        cartItemDto.setProductId(product.getId());
        cartItemDto.setQuantity(quantity);
        cartItemDto.setCreationDate(creationDate);
        return cartItemDto;
    }


}
