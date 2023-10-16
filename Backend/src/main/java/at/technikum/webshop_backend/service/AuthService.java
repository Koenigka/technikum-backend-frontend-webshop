package at.technikum.webshop_backend.service;

import at.technikum.webshop_backend.model.LoginResponse;
import at.technikum.webshop_backend.model.User;
import at.technikum.webshop_backend.security.JwtIssuer;
import at.technikum.webshop_backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtIssuer jwtIssuer;
    private final UserService userService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtIssuer jwtIssuer, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtIssuer = jwtIssuer;
        this.userService = userService;
    }


    /**
     * Handles user login by authenticating the user and generating an access token.
     *
     * @param email    The user's email for authentication.
     * @param password The user's password for authentication.
     * @return A {@link LoginResponse} containing the generated access token upon successful authentication.
     * @throws BadCredentialsException   If the provided credentials are invalid.
     * @throws AccessDeniedException     If the user is inactive and access is denied.
     */
    public LoginResponse login(String email, String password) {

        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
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
                throw new AccessDeniedException("User inactive");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");

        }
    }
}
