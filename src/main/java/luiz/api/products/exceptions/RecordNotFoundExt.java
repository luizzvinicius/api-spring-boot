package luiz.api.products.exceptions;

public class RecordNotFoundExt extends RuntimeException {
    public RecordNotFoundExt(String entity) {
        super(String.format("{error: %s NOT FOUND}", entity));
    }
}