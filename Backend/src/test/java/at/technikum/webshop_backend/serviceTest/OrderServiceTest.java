package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.enums.Status;
import at.technikum.webshop_backend.model.*;
import at.technikum.webshop_backend.repository.OrderItemRepository;
import at.technikum.webshop_backend.repository.OrderRepository;
import at.technikum.webshop_backend.service.CartService;
import at.technikum.webshop_backend.service.OrderService;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateOrderFromCart() {
        // Erstelle ein Beispiel-User-Objekt
        User user = new User();
        user.setId(1L);

        // Erstelle eine Liste von Beispiel-CartItems
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(new Product());
        cartItem1.setQuantity(2);
        cartItems.add(cartItem1);

        // Mock das erwartete Verhalten von userService.findById
        when(userService.findById(1L)).thenReturn(user);

        // Mock das erwartete Verhalten von cartService.viewCart
        when(cartService.viewCart(1L)).thenReturn(cartItems);

        // Erstelle ein Beispiel-OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem1.getProduct());
        orderItem.setQuantity(2);

        // Mock das erwartete Verhalten von orderItemRepository.save
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        // Erstelle ein Beispiel-CustomerOrder-Objekt
        CustomerOrder newCustomerOrder = new CustomerOrder();
        newCustomerOrder.setUser(user);
        newCustomerOrder.setOrderDate(LocalDateTime.now());
        newCustomerOrder.setStatus(Status.IN_PROGRESS);

        // Mock das erwartete Verhalten von orderRepository.save
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(newCustomerOrder);

        // Rufe die Methode createOrderFromCart auf
        CustomerOrder resultOrder = orderService.createOrderFromCart(1L);

        // Überprüfe das Ergebnis
        assertNotNull(resultOrder);
        assertEquals(user, resultOrder.getUser());
        assertEquals(Status.IN_PROGRESS, resultOrder.getStatus());
        assertEquals(cartItems.size(), resultOrder.getOrderItems().size());
    }
}
