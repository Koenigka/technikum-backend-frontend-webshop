package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.ProductDto;
import at.technikum.webshop_backend.dto.UserDto;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements ConvertableToDto<UserDto>, Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "title")
    private String title;

    @NotBlank
    @Length(min = 2, max = 80)
    @Column(name= "firstname")
    private String firstname;

    @NotBlank
    @Length(min = 2, max = 80)
    @Column(name = "lastname")
    private String lastname;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @NotBlank
    @Length(min = 3, max = 40)
    @Column(name = "username")
    private String username;

    @Email
    @Column(name = "email", unique = true, nullable = false)
    private String email;


    @Size(min = 5, message = "The password should have at least 5 characters")
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "isActive")
    private Boolean isActive;

    // TODO - Implement a List of Roles instead of a single role
    private String roles = "ROLE_USER";


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<CustomerOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<CartItem> cartItems = new HashSet<>();

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);

        //System.out.println("Password: " + this.password);
    }

    public Boolean getIsActive() {
        return isActive;
    }


    @Override
    public UserDto convertToDto() {
            UserDto userDto = new UserDto();
            userDto.setId(id);
            userDto.setTitle(title);
            userDto.setFirstname(firstname);
            userDto.setLastname(lastname);
            userDto.setAddress_id(address.getId());
            userDto.setAddress(address.getAddress());
            userDto.setCity(address.getCity());
            userDto.setZip(address.getZip());
            userDto.setState(address.getState());
            userDto.setUsername(username);
            userDto.setEmail(email);
            userDto.setIsActive(isActive);
            userDto.setRoles(roles);
            userDto.setPassword(null);
            return userDto;
            }


}
