package luiz.api.products.exceptions;

public class InvalidEnumEx extends RuntimeException {
    public  InvalidEnumEx(String enumName, String value) {
        super(String.format("{error: unexpected value (%s) for enum %s}", value, enumName));
    }
}