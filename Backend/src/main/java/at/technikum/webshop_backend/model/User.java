package at.technikum.webshop_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity(name = "user")
public class User {

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
    private Boolean isActive = true;

    private String role = "USER";

    public User() {  // default constructor for jpa
    }

    public User(Long id, String title, String firstname, String lastname, String address, String city, int zip, String username, String email, String password, Boolean isActive, String role) {
        this.id = id;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    // /////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
