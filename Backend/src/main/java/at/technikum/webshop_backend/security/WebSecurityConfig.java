package at.technikum.webshop_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class that defines the security settings for the application.
 * It enables global method security annotations and configures authentication and authorization rules.
 */
@Configuration
@EnableGlobalMethodSecurity(
prePostEnabled = true,
securedEnabled = true,
jsr250Enabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {



    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;

    /**
     * Configures the security filter chain for the application.
     *
     * @param http The HTTP security configuration.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception{

            http
                    .cors().disable()
                    .csrf().disable();

            http
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .formLogin().disable()
                    .securityMatcher("/**")
                    .authorizeHttpRequests(registry -> registry
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/api/auth/login").permitAll()
                            .requestMatchers( "/api/users/register").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/products").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/products/**").permitAll()
                            .requestMatchers( HttpMethod.GET, "/api/categories/**", "/api/categories/isActive/true").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/files/**").permitAll()
                            .anyRequest().authenticated()

                    );

        return http.build();

    }

    /**
     * Creates a password encoder bean for encoding and verifying passwords.
     *
     * @return The configured password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager for the application.
     *
     * @param http The HTTP security configuration.
     * @return The configured authentication manager.
     * @throws Exception If an error occurs while configuring authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder())
                .and().build();

    }
}
