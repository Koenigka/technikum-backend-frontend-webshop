package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.CartItemRepository;
import at.technikum.webshop_backend.service.CartService;
import at.technikum.webshop_backend.service.ProductService;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddToCart() {
        // Erstelle ein Beispiel-CartItemDto
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setUserId(1L);
        cartItemDto.setProductId(2L);
        cartItemDto.setQuantity(3);

        // Erstelle ein Beispiel-CartItem
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(3);

        // Mock das erwartete Verhalten von userService.findById
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        // Mock das erwartete Verhalten von productService.findById
        Product product = new Product();
        product.setId(2L);
        when(productService.findById(2L)).thenReturn(Optional.of(product));

        // Mock das erwartete Verhalten von cartItemRepository.findByUserAndProduct
        when(cartItemRepository.findByUserAndProduct(user, Optional.of(product))).thenReturn(null);

        // Mock das erwartete Verhalten von cartItemRepository.save
        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(1L);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedCartItem);

        // Rufe die Methode addToCart auf
        CartItem resultCartItem = cartService.addToCart(cartItemDto);

        // Überprüfe das Ergebnis
        assertNotNull(resultCartItem);
        assertEquals(user, resultCartItem.getUser());
        assertEquals(product, resultCartItem.getProduct());
        assertEquals(3, resultCartItem.getQuantity());
        assertNotNull(resultCartItem.getCreationDate());
    }
}
