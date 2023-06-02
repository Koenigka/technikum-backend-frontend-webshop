package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.model.LoginRequest;
import at.technikum.webshop_backend.model.LoginResponse;
import at.technikum.webshop_backend.security.JwtIssuer;
import at.technikum.webshop_backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtIssuer jwtIssuer;

    private final AuthenticationManager authenticationManager;

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
            var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

            return LoginResponse.builder()
                    .accessToken(token)
                    .build();

        } catch( AuthenticationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
