package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.LoginRequest;
import at.technikum.webshop_backend.model.LoginResponse;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.security.JwtIssuer;
import at.technikum.webshop_backend.security.UserPrincipal;
import at.technikum.webshop_backend.service.AuthService;
import at.technikum.webshop_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code AuthController} class handles authentication-related operations, such as user login.
 * It exposes an endpoint for user login and returns an access token upon successful authentication.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtIssuer jwtIssuer;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final AuthService authService;


    /**
     * Handles user login requests. It authenticates the user and generates an access token upon successful login.
     *
     * @param request The login request containing user credentials (email and password).
     * @return A {@link LoginResponse} object containing the generated access token.
     */
    @PostMapping("/api/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

}
