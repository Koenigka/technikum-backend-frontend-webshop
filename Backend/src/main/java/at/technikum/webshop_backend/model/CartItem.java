package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    private int quantity;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;



    @Override
    public CartItemDto convertToDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(id);
        cartItemDto.setUserId(user.getId());
        cartItemDto.setProductId(product.getId());
        cartItemDto.setTitle(product.getTitle());
        cartItemDto.setDescription((product.getDescription()));
        cartItemDto.setImg(product.getImg());
        cartItemDto.setPrice(product.getPrice());
        cartItemDto.setQuantity(quantity);
        cartItemDto.setCreationDate(creationDate);
        return cartItemDto;
    }


}
