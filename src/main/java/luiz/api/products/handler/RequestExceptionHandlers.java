package luiz.api.products.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandlers {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> treat404() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{'message': 'product not found'}");
    }
}