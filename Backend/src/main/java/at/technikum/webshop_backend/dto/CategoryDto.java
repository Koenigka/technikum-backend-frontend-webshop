package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.utils.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO for {@link Category}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements DataTransferObject {

    private long id;

    private String title;

    private String description;

    private String imgUrl;

    private Boolean active;
}
