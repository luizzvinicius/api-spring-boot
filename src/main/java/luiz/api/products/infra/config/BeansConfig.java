package luiz.api.products.infra.config;

import luiz.api.products.core.gateways.ProductGateway;
import luiz.api.products.core.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public CreateProductUseCaseI createProduct(ProductGateway gateway) {
        return new CreateProductImp(gateway);
    }

    @Bean
    public GetProductsUseCaseI getProducts(ProductGateway gateway) {
        return new GetProductsUseCaseImp(gateway);
    }

    @Bean
    public GetProductUseCaseI getProduct(ProductGateway gateway) {
        return new GetProductUseCaseImp(gateway);
    }

    @Bean
    public UpdateProductUseCaseI updateProduct(ProductGateway gateway) {
        return new UpdateProductUseCaseImp(gateway);
    }
}