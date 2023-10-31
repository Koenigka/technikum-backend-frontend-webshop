package at.technikum.webshop_backend.serviceTest;

import at.technikum.webshop_backend.model.LoginResponse;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.security.JwtIssuer;
import at.technikum.webshop_backend.security.UserPrincipal;
import at.technikum.webshop_backend.service.AuthService;
import at.technikum.webshop_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtIssuer jwtIssuer;

    @Mock
    private UserService userService;

    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(authenticationManager, jwtIssuer, userService);
    }

    @Test
    public void testSuccessfulLogin() {
        String email = "test@example.com";
        String password = "password";
        String token = "mocked_token";

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .email(email)
                .password("hashed_password")
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenReturn(authentication);

        User user = new User();
        user.setIsActive(true);

        when(userService.findById(anyLong())).thenReturn(user);
        when(jwtIssuer.issue(anyLong(), anyString(), anyList())).thenReturn(token);

        LoginResponse response = authService.login(email, password);

        assertEquals(token, response.getAccessToken());
    }

    @Test
    public void testFailedLogin() {
        String email = "test@example.com";
        String password = "password";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(email, password));
    }

    @Test
    public void testInactiveUserLogin() {
        String email = "test@example.com";
        String password = "password";

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .userId(1L)
                .email(email)
                .password("hashed_password")
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))).thenReturn(authentication);

        User user = new User();
        user.setIsActive(false);

        when(userService.findById(anyLong())).thenReturn(user);

        assertThrows(AccessDeniedException.class, () -> authService.login(email, password));
    }
}

