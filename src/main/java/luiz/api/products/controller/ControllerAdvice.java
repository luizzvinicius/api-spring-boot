package luiz.api.products.controller;

import jakarta.validation.ConstraintViolationException;
import luiz.api.products.exceptions.InvalidEnumEx;
import luiz.api.products.exceptions.RecordNotFoundExt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RecordNotFoundExt.class)
    public ResponseEntity<String> handle404(RecordNotFoundExt e) {
        return ResponseEntity.status(NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidEnumEx.class)
    public ResponseEntity<String> handle404(InvalidEnumEx e) {
        return ResponseEntity.status(NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleJsonValueInvalid(MethodArgumentNotValidException ex) {
        var fields = ex.getFieldErrors().stream()
                .map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
                .reduce("", (c, e) -> c + e + ", \n");

        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                .body(String.format("{error: \"invalid request pattern\", message: \"%s\"}", fields));
    }

    @ExceptionHandler(ConstraintViolationException.class) // url params
    public ResponseEntity<String> handleValidationDTOs(ConstraintViolationException e) {
        var fields = e.getConstraintViolations().stream()
                .map(error -> String.format("%s %s", error.getPropertyPath(), error.getMessage()))
                .reduce("", (acc, error) -> acc + error + ", \n");

        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                .body(String.format("{error: \"invalid request pattern\", message: \"%s\"}", fields));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // JSON value invalid
    public ResponseEntity<String> missingAttribute(HttpMessageNotReadableException e) {
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                .body(String.format("{error: \"Bad Request\", message: \"%s\"}", e.getMessage()));
    }

    // Database
    @ExceptionHandler(DataIntegrityViolationException.class) // duplicate registers etc
    public ResponseEntity<String> integrityViolation() {
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                .body("{error: \"Bad Request\", message: \"database error\"}");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> methodNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(APPLICATION_JSON)
                .body("{error: \"Method Not Allowed\", message: \"Endpoint with this method is invalid\"}");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // type url param
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        var types = new String[]{"Null"};
        var name = "Null";
        if (e != null) {
            types = e.getRequiredType().getName().split("\\.");
            name = e.getName();
        }

        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                .body(String.format("{error: \"BAD_REQUEST\", message: \"%s should be of type %s\"", name, types[types.length - 1]));
    }
}