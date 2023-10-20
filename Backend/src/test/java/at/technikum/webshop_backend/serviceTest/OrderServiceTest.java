package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.CustomerOrderDto;
import at.technikum.webshop_backend.enums.Status;
import at.technikum.webshop_backend.model.*;
import at.technikum.webshop_backend.repository.OrderItemRepository;
import at.technikum.webshop_backend.repository.OrderRepository;
import at.technikum.webshop_backend.service.CartService;
import at.technikum.webshop_backend.service.EntityNotFoundException;
import at.technikum.webshop_backend.service.OrderService;
import at.technikum.webshop_backend.service.ProductService;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductService productService;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, cartService, userService, orderItemRepository, productService);

        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        when(productService.findById(1L)).thenReturn(Optional.of(product));

    }

    @Test
    public void testCreateOrderFromCart() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        when(userService.findById(userId)).thenReturn(user);

        when(cartService.viewCart(userId)).thenReturn(Collections.singletonList(cartItem));

        CustomerOrder savedOrder = new CustomerOrder();
        savedOrder.setUser(user);
        savedOrder.setId(1L);
        savedOrder.setStatus(Status.IN_PROGRESS);
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(savedOrder);

        CustomerOrder result = orderService.createOrderFromCart(userId);

        verify(userService, times(1)).findById(userId);
        verify(cartService, times(1)).viewCart(userId);
        verify(orderRepository, times(2)).save(any(CustomerOrder.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }


    @Test
    public void testCreateOrderFromCartUserNotFound() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrderFromCart(userId);
        });

        assertEquals("User not found.", exception.getMessage()); // Update the expected message
    }

    @Disabled
    @Test
    public void testCheckProductQuantities() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setStock(5);

        CartItem cartItem1 = new CartItem();
        cartItem1.setUser(user);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(3);

        when(cartService.viewCart(userId)).thenReturn(Collections.singletonList(cartItem1));

        Map<String, Integer> insufficientQuantities = orderService.checkProductQuantities(userId);

        verify(cartService, times(1)).viewCart(userId);

        assertEquals(1, insufficientQuantities.size());
        assertEquals(2, insufficientQuantities.get("Product 1"));
    }



    @Test
    public void testGetAvailableProductQuantity() {
        ProductService productService = mock(ProductService.class);

        Long productId = 1L;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setStock(10);

        when(productService.findById(productId)).thenReturn(Optional.of(expectedProduct));

        OrderService orderService = new OrderService(orderRepository, cartService, userService, orderItemRepository, productService);

        int availableQuantity = orderService.getAvailableProductQuantity(productId);

        assertEquals(10, availableQuantity);
    }


    @Test
    public void testDeductProductQuantitySufficientStock() {
        Long productId = 1L;
        int initialStock = 10;
        int quantityToDeduct = 3;

        Product product = new Product();
        product.setId(productId);
        product.setStock(initialStock);

        when(productService.findById(productId)).thenReturn(Optional.of(product));

        orderService.deductProductQuantity(productId, quantityToDeduct);

        assertEquals(initialStock - quantityToDeduct, product.getStock());

        verify(productService, times(1)).save(product);
    }

    @Test
    public void testGetUserOrdersByUserIdUserExists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        CustomerOrder order1 = new CustomerOrder();
        order1.setId(1L);
        order1.setUser(user);

        CustomerOrder order2 = new CustomerOrder();
        order2.setId(2L);
        order2.setUser(user);

        List<CustomerOrder> expectedOrders = Arrays.asList(order1, order2);

        when(userService.findById(userId)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(expectedOrders);

        List<CustomerOrder> actualOrders = orderService.getUserOrdersByUserId(userId);

        assertEquals(expectedOrders, actualOrders);
    }


    @Test
    public void testGetUserOrderByOrderIdOrderExists() {
        Long orderId = 1L;
        CustomerOrder expectedOrder = new CustomerOrder();
        expectedOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        CustomerOrder actualOrder = orderService.getUserOrderByOrderId(orderId);

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGetUserOrderByOrderIdOrderNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CustomerOrder actualOrder = orderService.getUserOrderByOrderId(orderId);

        assertNull(actualOrder);
    }


    @Test
    public void testFindOrdersByFiltersWithEmailFilter() {
        // Erstellen Sie einen Filter mit E-Mail-Kriterium
        Map<String, String> filters = new HashMap<>();
        filters.put("email", "user@example.com");

        // Erstellen Sie eine Beispiel-Bestellung mit passender E-Mail
        User user = new User();
        user.setEmail("user@example.com");
        CustomerOrder expectedOrder = new CustomerOrder();
        expectedOrder.setUser(user);

        when(orderRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(expectedOrder));

        List<CustomerOrder> actualOrders = orderService.findOrdersByFilters(filters);

        // Überprüfen Sie, ob die erwartete Bestellung in der Ergebnisliste enthalten ist
        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrder, actualOrders.get(0));
    }

    @Test
    public void testFindOrdersByFiltersWithOrderIdFilter() {
        Map<String, String> filters = new HashMap<>();
        filters.put("orderId", "1");

        CustomerOrder expectedOrder = new CustomerOrder();
        expectedOrder.setId(1L);

        when(orderRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(expectedOrder));

        List<CustomerOrder> actualOrders = orderService.findOrdersByFilters(filters);

        assertEquals(1, actualOrders.size());
        assertEquals(expectedOrder, actualOrders.get(0));
    }


    @Test
    public void testUpdateOrder() {
        CustomerOrderDto orderDto = new CustomerOrderDto();
        orderDto.setId(1L);
        orderDto.setStatus(Status.SHIPPED);

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(1L);
        customerOrder.setStatus(Status.IN_PROGRESS);

        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        orderItem.setProduct(product);
        orderItem.setQuantity(3);
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);
        customerOrder.setOrderItems(orderItems);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(customerOrder));

        when(orderRepository.save(any(CustomerOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerOrder updatedOrder = orderService.updateOrder(orderDto);

        assertEquals(orderDto.getStatus(), updatedOrder.getStatus());

        if (orderDto.getStatus() == Status.CANCELED) {
            assertEquals(13, product.getStock());
        } else {
            assertEquals(10, product.getStock());
        }
    }

}
