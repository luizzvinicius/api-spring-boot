package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;
import luiz.api.products.core.gateways.ProductGateway;

public class UpdateProductUseCaseImp implements UpdateProductUseCaseI{
    private final ProductGateway gateway;

    public UpdateProductUseCaseImp(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Product updateProduct(Product p) {
        return gateway.updateProduct(p);
    }
}