package at.technikum.webshop_backend.model;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.dto.CustomerOrderDto;
import at.technikum.webshop_backend.enums.Status;
import at.technikum.webshop_backend.utils.ConvertableToDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity (name = "customerOrder")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrder implements ConvertableToDto<CustomerOrderDto>, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "customerOrder")
    private Set<OrderItem> orderItems = new HashSet<>();



    @Override
    public CustomerOrderDto convertToDto() {
        CustomerOrderDto customerOrderDto = new CustomerOrderDto();
        customerOrderDto.setId(id);
        customerOrderDto.setUserId(user.getId());
        customerOrderDto.setFirstName(user.getFirstname());
        customerOrderDto.setLastName(user.getLastname());
        customerOrderDto.setEmail(user.getEmail());
        customerOrderDto.setAddress(user.getAddress().getAddress());
        customerOrderDto.setCity(user.getAddress().getCity());
        customerOrderDto.setZip(user.getAddress().getZip());
        customerOrderDto.setOrderDate(orderDate);
        customerOrderDto.setStatus(status);
        customerOrderDto.setOrderItems(orderItems.stream().map(OrderItem::convertToDto).collect(Collectors.toSet()));
        return customerOrderDto;
    }
}
