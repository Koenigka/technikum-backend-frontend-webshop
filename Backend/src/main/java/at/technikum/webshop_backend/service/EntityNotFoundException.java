package at.technikum.webshop_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a specific entity cannot be found in the system.
 * 
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity not found")
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String s) {
        super(s + " not found.");
    }
}
