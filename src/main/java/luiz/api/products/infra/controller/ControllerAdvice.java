package luiz.api.products.infra.controller;

import jakarta.validation.ConstraintViolationException;
import luiz.api.products.core.exceptions.InvalidEnumEx;
import luiz.api.products.core.exceptions.RecordNotFoundExt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RecordNotFoundExt.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(RecordNotFoundExt e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidEnumEx.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(InvalidEnumEx e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // nome atributo inválido
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationDTOs(MethodArgumentNotValidException e) {
        var fields = e.getFieldErrors().stream()
                .map(error -> String.format("'%s %s'", error.getField(), error.getDefaultMessage()))
                .reduce("", (acc, error) -> acc + error + ", \n");
        return String.format("{'error': 'invalid request pattern',%n'message': %s}", fields);
    }

    @ExceptionHandler(ConstraintViolationException.class) // url params
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationDTOs(ConstraintViolationException e) {
        var fields = e.getConstraintViolations().stream()
                .map(error -> String.format("'%s %s'", error.getPropertyPath(), error.getMessage()))
                .reduce("", (acc, error) -> acc + error + ", \n");
        return String.format("{'error': 'invalid request pattern',%n'message': %s}", fields);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // JSON value invalid
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String missingAttribute(HttpMessageNotReadableException e) {
        return String.format("{'error': 'Bad Request',%n'message': %s}", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class) // registro duplicado etc.
    public ResponseEntity<String> integrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{'error': 'Bad Request',\n'message': database error}");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String methodNotAllowed() {
        return "{'error': 'Method Not Allowed',\n'message': 'Endpoint with this method is invalid'}";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // type url param
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        if (e != null) {
            var types = e.getRequiredType().getName().split("\\.");
            return String.format("{'error': 'Bad Request',%n'message': '%s should be of type %s'}", e.getName(), types[types.length - 1]);
        }
        return "{'error': 'Bad request'";
    }
}