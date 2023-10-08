package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.model.Category;
import at.technikum.webshop_backend.utils.DataTransferObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "The 'title' field must not be blank.")
    @Size(max = 255, message = "The 'title' field must not exceed 255 characters.")
    private String title;

    @NotBlank(message = "The 'description' field must not be blank.")
    @Size(max = 1000, message = "The 'description' field must not exceed 1005 characters.")
    private String description;

    @NotBlank(message = "The 'img' field must not be blank.")
    @Size(max = 1000, message = "The 'img' field must not exceed 1000 characters.")
    private String imgUrl;

    @NotNull(message = "The 'active' field must not be null.")
    private Boolean active;
}
