package at.technikum.webshop_backend.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code JwtProperties} class is a configuration class for settings related to JWTs (JSON Web Tokens).
 * In this case, it holds the secret key used for JWT creation and verification.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("security.jwt")
public class JwtProperties {

    /**
     * The secret key used for JWT creation and verification.
     */
    private String secretKey;
}
