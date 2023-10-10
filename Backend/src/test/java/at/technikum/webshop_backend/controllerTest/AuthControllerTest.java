package at.technikum.webshop_backend.controllerTest;

import at.technikum.webshop_backend.controller.AuthController;
import at.technikum.webshop_backend.model.LoginRequest;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.security.JwtIssuer;
import at.technikum.webshop_backend.security.UserPrincipal;
import at.technikum.webshop_backend.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtIssuer jwtIssuer;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(jwtIssuer, authenticationManager, userService)).build();
    }

    @Test
    public void testLogin() throws Exception {
        // Mock the UserPrincipal
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .email("test@example.com")
                .password("hashed_password")
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
        // Mock the Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        // Mock the AuthenticationManager behavior
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);


        // Mock the JwtIssuer behavior
        String token = generateTestToken();
        when(jwtIssuer.issue(any(), any(), any())).thenReturn(token);

        // Mock the UserService behavior
        User user = new User();
        user.setIsActive(true);
        when(userService.findById(any())).thenReturn(user);


        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value(token));
    }

    // Helper method to generate a test JWT token
    private String generateTestToken() {
        Algorithm algorithm = Algorithm.HMAC256("your-secret-key"); // Replace with your actual secret key
        return JWT.create()
                .withSubject("1")
                .withClaim("e", "test@example.com")
                .withClaim("a", List.of("ROLE_USER"))
                .sign(algorithm);
    }
}
