package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.model.Address;
import at.technikum.webshop_backend.utils.DataTransferObject;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements DataTransferObject {


    private Long id;

    private String title;

    private String firstname;

    private String lastname;

    private Long address_id;
    private String address;
    private String city;

    private int zip;

    private String username;

    private String password;


    private String email;

    private Boolean isActive;
    private String role = "USER";

}
