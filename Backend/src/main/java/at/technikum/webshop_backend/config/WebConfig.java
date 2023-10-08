package at.technikum.webshop_backend.config;

import jakarta.servlet.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * This filter class configures CORS (Cross-Origin Resource Sharing) to allow cross-origin requests.
 * It sets the necessary HTTP response headers to enable CORS for a Spring Boot web application.
 * CORS is used to control requests made to a different origin (e.g., domain or port) from the one
 * serving the web application.
 *
 * Note: In a production environment, it is important to configure CORS headers carefully to avoid
 * security vulnerabilities. Allowing unrestricted access using "*" ("Access-Control-Allow-Origin")
 * may expose your application to potential security risks. It is recommended to specify trusted
 * origins explicitly instead of using "*" (e.g. response.setHeader("Access-Control-Allow-Origin",
 * "https://trusted-domain1.com, https://trusted-domain2.com, https://trusted-domain3.com");.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebConfig implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // Prod ....
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
