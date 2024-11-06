package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;
import luiz.api.products.core.gateways.ProductGateway;

import java.util.List;

public class GetProductsUseCaseImp implements GetProductsUseCaseI {
    private final ProductGateway gateway;

    public GetProductsUseCaseImp(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public List<Product> getAllProducts(int page, int quantity) {
        return gateway.getAllProducts(page, quantity);
    }
}