
package at.technikum.webshop_backend.controllerTest;
/*
import at.technikum.webshop_backend.dto.CartItemDto;
import at.technikum.webshop_backend.model.CartItem;
import at.technikum.webshop_backend.model.Product;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    public void testAddToCartAuthenticated() throws Exception {
        // Mock the authentication context to simulate an authenticated user
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Mock the cartService to return a CartItem
        CartItem cartItem = new CartItem(); // Create a CartItem instance as needed
        // Create a test User and set it as the user of the cartItem
        User user = new User();
        user.setId(1L); // Set the ID of the user
        cartItem.setUser(user); // Set the user for the cartItem

        // Create a test Product
        Product product = new Product();
        product.setId(1L); // Set the ID of the product
        cartItem.setProduct(product);

        // Set other product properties
        when(cartService.addToCart(any(CartItemDto.class))).thenReturn(cartItem);

        // Create a CartItemDto for testing
        CartItemDto cartItemDto = new CartItemDto(); // Create a CartItemDto instance as needed

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add")
                        .content(new ObjectMapper().writeValueAsString(cartItemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}*/
