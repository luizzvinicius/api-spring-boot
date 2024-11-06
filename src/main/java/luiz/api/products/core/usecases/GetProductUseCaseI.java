package luiz.api.products.core.usecases;

import luiz.api.products.core.entities.Product;

import java.util.Optional;
import java.util.UUID;

public interface GetProductUseCaseI {
    Optional<Product> get(UUID id);
}