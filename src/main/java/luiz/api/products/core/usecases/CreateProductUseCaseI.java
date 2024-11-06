package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;

public interface CreateProductUseCaseI {
    Product save(Product p);
}