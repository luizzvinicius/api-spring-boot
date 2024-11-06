package luiz.api.products.core.usecases;

import java.util.UUID;

public interface DeleteProductUseCaseI {
    int deleteProduct(UUID id);
}