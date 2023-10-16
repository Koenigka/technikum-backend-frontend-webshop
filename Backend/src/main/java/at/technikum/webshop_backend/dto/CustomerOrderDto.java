package at.technikum.webshop_backend.dto;

import at.technikum.webshop_backend.enums.Status;
import at.technikum.webshop_backend.utils.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderDto implements DataTransferObject {

    private Long id;
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String address;

    private String city;

    private int zip;

    private LocalDateTime orderDate;
    private Status status;
    private Set<OrderItemDto> orderItems;
}
