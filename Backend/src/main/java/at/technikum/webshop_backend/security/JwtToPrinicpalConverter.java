package at.technikum.webshop_backend.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@code JwtToPrinicpalConverter} class is responsible for converting a decoded JWT
 * (JSON Web Token) into a {@link UserPrincipal} object that represents the user contained in the JWT.
 * It extracts user-specific information such as user ID, email, and authorities from the JWT.
 */
@Component
public class JwtToPrinicpalConverter {

    /**
     * Converts a decoded JWT into a {@link UserPrincipal} object.
     *
     * @param jwt The decoded JWT to convert.
     * @return A {@link UserPrincipal} object representing the user from the JWT.
     */
    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .email(jwt.getClaim("e").asString())
                .authorities(extractAuthoritiesFromClaim(jwt))
                .build();
    }

    /**
     * Extracts authorities (roles or permissions) from a claim in the JWT.
     *
     * @param jwt The decoded JWT from which authorities are extracted.
     * @return A list of {@link SimpleGrantedAuthority} objects representing user roles or permissions.
     */
    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim (DecodedJWT jwt) {

        var claim = jwt.getClaim("a");
        if (claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }
}
