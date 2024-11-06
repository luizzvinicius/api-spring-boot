package luiz.api.products.core.enums;

public enum ProductStatus {
    ACTIVE("ativo"), INACTIVE("inativo");
    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }
}