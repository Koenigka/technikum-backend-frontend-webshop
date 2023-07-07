package at.technikum.webshop_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//The most straightforward way to get started
// using Spring Boot is to create a main class and annotate it with @SpringBootApplication.
// By default, it will scan all the components in the same package or below.
@SpringBootApplication
public class WebshopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebshopBackendApplication.class, args);



	}

}
