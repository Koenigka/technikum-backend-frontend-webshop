package at.technikum.webshop_backend.controller;


import at.technikum.webshop_backend.model.CustomerOrder;
import at.technikum.webshop_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    private OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/create")
    public ResponseEntity<CustomerOrder> createOrder(@RequestParam Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {

            CustomerOrder newCustomerOrder = orderService.createOrderFromCart(userId);

            return ResponseEntity.ok(newCustomerOrder);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }

    //other routes for order handling go here

}
