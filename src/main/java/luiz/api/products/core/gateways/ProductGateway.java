package luiz.api.products.core.gateways;

import luiz.api.products.core.entities.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {
    List<Product> getAllProducts(int page, int quantity);

    Product saveProduct(Product p);

    Optional<Product> getOneProduct(UUID id);

    Product updateProduct(Product p);

    int deleteProduct(UUID id);
}