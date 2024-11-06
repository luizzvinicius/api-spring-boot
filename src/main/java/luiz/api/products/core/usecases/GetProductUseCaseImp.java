package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;
import luiz.api.products.core.gateways.ProductGateway;

import java.util.Optional;
import java.util.UUID;

public class GetProductUseCaseImp implements GetProductUseCaseI {
    private final ProductGateway gateway;

    public GetProductUseCaseImp(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Optional<Product> get(UUID id) {
        return gateway.getOneProduct(id);
    }
}