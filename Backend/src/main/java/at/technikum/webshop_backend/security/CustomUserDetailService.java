package at.technikum.webshop_backend.security;

import at.technikum.webshop_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Custom implementation of the Spring Security UserDetailsService interface.
 * It loads user details from the UserService based on their email.
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    /**
     * Load user details by user's email.
     *
     * @param username Username of the user.
     * @return UserDetails containing user information and authorities.
     * @throws UsernameNotFoundException Thrown if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findByEmail(username).orElseThrow();

        return UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRoles())))
                .password(user.getPassword())
                .build();
    }
}
