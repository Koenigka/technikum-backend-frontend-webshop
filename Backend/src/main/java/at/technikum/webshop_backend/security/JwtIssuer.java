package at.technikum.webshop_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The JwtIssuer class is responsible for issuing JWT tokens.
 *
 */
@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final JwtProperties props;


    /**
     * Issues a JWT token for the given user.
     *
     * @param userId The unique identifier of the user.
     * @param email The email address of the user.
     * @param roles The roles or permissions associated with the user.
     * @return The JWT token as a string.
     */
    public String issue(Long userId, String email, List<String> roles) {

        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim("e", email)
                .withClaim("a", roles)
                .sign(Algorithm.HMAC256 (props.getSecretKey()));

    }
}
