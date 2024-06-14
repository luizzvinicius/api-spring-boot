package luiz.api.products.enums;

public enum ProductStatus {
    ACTIVE("ativo"), INACTIVE("inativo");
    private final String status;

    private ProductStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}