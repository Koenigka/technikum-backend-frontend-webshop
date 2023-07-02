package at.technikum.webshop_backend.dto;

import jakarta.validation.constraints.NotNull;

public class PositionDTO {

    private Long id;


    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
