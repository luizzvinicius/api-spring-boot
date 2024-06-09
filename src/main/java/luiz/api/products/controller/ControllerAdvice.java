package luiz.api.products.controller;

import luiz.api.products.exceptions.RecordNotFoundExt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RecordNotFoundExt.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(RecordNotFoundExt e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // nome atributo inválido
    public ResponseEntity<String> handleValidationDTOs() {
        return ResponseEntity.badRequest().body("{'error': 'invalid request pattern'}");
    }
}