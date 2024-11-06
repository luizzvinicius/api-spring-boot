package luiz.api.products.core.exceptions;

public class RecordNotFoundExt extends RuntimeException {
    public RecordNotFoundExt(String entity) {
        super(String.format("{error: %s NOT FOUND}", entity));
    }
}