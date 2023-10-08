package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.LoginRequest;
import at.technikum.webshop_backend.model.LoginResponse;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.security.JwtIssuer;
import at.technikum.webshop_backend.security.UserPrincipal;
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

    /**
     * Handles user login requests. It authenticates the user and generates an access token upon successful login.
     *
     * @param request The login request containing user credentials (email and password).
     * @return A {@link LoginResponse} object containing the generated access token.
     */
    @PostMapping("/api/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {

        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            var authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            var principal = (UserPrincipal) authentication.getPrincipal();
            var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            User user = userService.findById(principal.getUserId());

           if (user != null && user.getIsActive()) {
                var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

                return LoginResponse.builder()
                        .accessToken(token)
                        .build();
           } else {
                throw new DisabledException("User inactive");
            }


        } catch( AuthenticationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
