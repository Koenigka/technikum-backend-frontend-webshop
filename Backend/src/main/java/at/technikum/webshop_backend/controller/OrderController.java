package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.dto.CustomerOrderDto;
import at.technikum.webshop_backend.model.CustomerOrder;

import at.technikum.webshop_backend.security.UserPrincipal;
import at.technikum.webshop_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The {@code OrderController} class handles HTTP requests related to order operations.
 * It exposes endpoints for creating orders.
 *
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {


    private OrderService orderService;

    private static final String authorityAdmin = "ROLE_ADMIN";


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Handles an HTTP POST request to create a new order.
     *
     * @return A ResponseEntity with an appropriate HTTP status code and response body.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            Long userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
            Map<String, Integer> insufficientQuantities = orderService.checkProductQuantities(userId);
            if (!insufficientQuantities.isEmpty()) {
                return ResponseEntity.badRequest().body(insufficientQuantities);
            } else {

                CustomerOrder newCustomerOrder = orderService.createOrderFromCart(userId);
                return ResponseEntity.ok(newCustomerOrder.convertToDto());
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }



    /**
     * Handles an HTTP GET request to retrieve a list of customer orders by user ID.
     *
     * @param userId The ID of the user for whom orders are to be retrieved.
     * @return A ResponseEntity with a list of CustomerOrderDto representing customer orders,
     *         and an appropriate HTTP status code, or a forbidden status if the requester is not an admin.
     */
    @GetMapping("/ordersByUserId/{userId}")
    public ResponseEntity<List<CustomerOrderDto>> getUserOrdersByUserId(@PathVariable Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        List<CustomerOrder> userOrders = orderService.getUserOrdersByUserId(userId);

        return ResponseEntity.ok(orderService.convertToOrderDtoList(userOrders));
    }


    /**
     * Handles an HTTP GET request to retrieve a customer order by its order ID.
     *
     * @param orderId The ID of the order to be retrieved.
     * @return A ResponseEntity with a CustomerOrderDto representing the customer order,
     *         and an appropriate HTTP status code, or a forbidden status if the requester is not an admin.
     */
    @GetMapping("/orderByOrderId/{orderId}")
    public ResponseEntity<CustomerOrderDto> getUserOrdersByOrderId(@PathVariable Long orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
       CustomerOrder userOrder = orderService.getUserOrderByOrderId(orderId);

        return ResponseEntity.ok(userOrder.convertToDto());
    }

    /**
     * Handles an HTTP POST request to search for customer orders based on specified filters.
     *
     * @param filters A map of key-value pairs representing filters for order search.
     * @return A ResponseEntity with a list of CustomerOrderDto representing orders matching the filters
     *         and an appropriate HTTP status code, or a forbidden status if the requester is not an admin.
     */
    @PostMapping("/search")
    public ResponseEntity<List<CustomerOrderDto>> searchOrders(@RequestBody Map<String, String> filters) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .anyMatch(val -> val.equals(authorityAdmin));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        List<CustomerOrder> orders = orderService.findOrdersByFilters(filters);

        List<CustomerOrderDto> orderDtoList = orderService.convertToOrderDtoList(orders);

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    /**
     * Handles an HTTP PUT request to update a customer order.
     *
     * @param orderDto The CustomerOrderDto containing the updated order information.
     * @return A ResponseEntity with the updated CustomerOrderDto and an appropriate HTTP status code,
     *         or a forbidden status if the requester is not an admin.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody CustomerOrderDto orderDto) {
        CustomerOrder updatedOrder = orderService.updateOrder(orderDto);
        CustomerOrderDto updatedOrderDto = updatedOrder.convertToDto();

        return ResponseEntity.ok(updatedOrderDto);
    }


    }
