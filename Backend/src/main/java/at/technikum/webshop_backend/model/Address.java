package at.technikum.webshop_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Entity(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 2, max = 100)
    @Column(name = "address")
    private String address;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name ="city")
    private String city;

    @NotNull
    @Column(name = "zip")
    private int zip;

    @NotBlank
    @Length(min = 2, max = 30)
    @Column(name ="state")
    private String state;
}
