package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;

import java.util.List;

public interface GetProductsUseCaseI {
    List<Product> getAllProducts(int page, int quantity);
}