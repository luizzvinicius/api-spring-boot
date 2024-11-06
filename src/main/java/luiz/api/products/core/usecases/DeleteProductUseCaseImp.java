package luiz.api.products.core.usecases;

import luiz.api.products.core.gateways.ProductGateway;

import java.util.UUID;

public class DeleteProductUseCaseImp implements DeleteProductUseCaseI {
    private final ProductGateway gateway;

    public DeleteProductUseCaseImp(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public int deleteProduct(UUID id) {
        return gateway.deleteProduct(id);
    }
}