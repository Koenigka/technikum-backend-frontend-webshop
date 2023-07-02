package at.technikum.webshop_backend.dto;


import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.utils.DataTransferObject;

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

   private String title;

    private String description;

    private String img;

    private double price;

    private int stock;

    private Boolean active;

    private Long categoryId;

}
