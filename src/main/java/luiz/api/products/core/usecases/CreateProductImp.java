package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;
import luiz.api.products.core.gateways.ProductGateway;

public class CreateProductImp implements CreateProductUseCaseI {
    private final ProductGateway gateway;

    public CreateProductImp(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Product save(Product p) {
        return gateway.saveProduct(p);
    }
}