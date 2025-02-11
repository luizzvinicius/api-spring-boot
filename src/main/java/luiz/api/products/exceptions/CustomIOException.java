package luiz.api.products.exceptions;

public class CustomIOException extends RuntimeException {
    public CustomIOException(String message) {
        super(String.format("{message: %s}", message));
    }
}