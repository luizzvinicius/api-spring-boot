package luiz.api.products.enums;

public enum ProductStatus {
    ACTIVE("ativo"), INACTIVE("inativo");
    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}