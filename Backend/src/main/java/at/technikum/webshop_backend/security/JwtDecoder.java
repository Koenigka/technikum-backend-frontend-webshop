package at.technikum.webshop_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The {@code JwtDecoder} class is responsible for decoding a JWT (JSON Web Token).
 * It uses the configuration settings provided in the {@code JwtProperties}, such as the secret key,
 * to verify the token's authenticity. After decoding, the JWT is verified and returned.
 */
@Component
@RequiredArgsConstructor
public class JwtDecoder {

    private final JwtProperties properties;

    /**
     * Decodes and verifies a JWT based on the configuration settings in {@code JwtProperties}.
     *
     * @param token The JWT to decode.
     * @return The decoded JWT (DecodedJWT) after successful verification.
     */
    public DecodedJWT decode(String token) {

        // Use the secret key from JwtProperties to verify the token.
        return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                .build()
                .verify(token);
    }
}
