package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.enums.Status;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.CustomerOrder;
import at.technikum.webshop_backend.model.OrderItem;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.OrderItemRepository;
import at.technikum.webshop_backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CartService cartService;
    private UserService userService;

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService, UserService userService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
    }

    public CustomerOrder createOrderFromCart(Long userId) {

        User user = userService.findById(userId);

        if (user == null) {
            throw new EntityNotFoundException("User");
        }

        List<CartItem> cartItems = cartService.viewCart(userId);

        System.out.println(cartItems.size());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Empty cart");
        }

        CustomerOrder newCustomerOrder = new CustomerOrder();
        newCustomerOrder.setUser(user);
        newCustomerOrder.setOrderDate(LocalDateTime.now());
        newCustomerOrder.setStatus(Status.IN_PROGRESS);

        newCustomerOrder = orderRepository.save(newCustomerOrder);


        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCustomerOrder(newCustomerOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);

            newCustomerOrder.getOrderItems().add(orderItem);

            System.out.println(orderItem.getId());



        }

        newCustomerOrder = orderRepository.save(newCustomerOrder);

        cartService.clearCartItems(cartItems);

        return newCustomerOrder;
    }
}
