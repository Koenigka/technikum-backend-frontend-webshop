package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.repository.CartItemRepository;
import at.technikum.webshop_backend.service.CartService;
import at.technikum.webshop_backend.service.EntityNotFoundException;
import at.technikum.webshop_backend.service.ProductService;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(cartItemRepository, userService, productService);
    }

    @Test
    public void testAddToCart() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setUserId(1L);
        cartItemDto.setProductId(2L);
        cartItemDto.setQuantity(3);

        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(2L);

        when(userService.findById(1L)).thenReturn(user);
        when(productService.findById(2L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByUserAndProduct(user, Optional.of(product))).thenReturn(null);
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItem addedCartItem = cartService.addToCart(cartItemDto);

        assertEquals(user, addedCartItem.getUser());
        assertEquals(product, addedCartItem.getProduct());
        assertEquals(cartItemDto.getQuantity(), addedCartItem.getQuantity());
    }

    @Test
    public void testAddToCartExistingItem() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setUserId(1L);
        cartItemDto.setProductId(2L);
        cartItemDto.setQuantity(3);

        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(2L);

        CartItem existingCartItem = new CartItem();
        existingCartItem.setUser(user);
        existingCartItem.setProduct(product);
        existingCartItem.setQuantity(5);

        when(userService.findById(1L)).thenReturn(user);

        when(productService.findById(2L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByUserAndProduct(user, Optional.of(product))).thenReturn(existingCartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItem addedCartItem = cartService.addToCart(cartItemDto);

        assertEquals(user, addedCartItem.getUser());
        assertEquals(product, addedCartItem.getProduct());
        assertEquals(8, addedCartItem.getQuantity()); // 5 (existing) + 3 (added)
    }


    @Test
    public void testAddToCartUserNotFound() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setUserId(1L);

        when(userService.findById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(cartItemDto));

        verify(userService, times(1)).findById(1L);
        verify(productService, never()).findById(anyLong());
        verify(cartItemRepository, never()).findByUserAndProduct(any(User.class), any(Optional.class));
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    public void testAddToCartProductNotFound() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setUserId(1L);
        cartItemDto.setProductId(2L);

        User user = new User();
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(productService.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(cartItemDto));

        verify(userService, times(1)).findById(1L);
        verify(productService, times(1)).findById(2L);
        verify(cartItemRepository, never()).findByUserAndProduct(any(User.class), any(Optional.class));
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }
}
