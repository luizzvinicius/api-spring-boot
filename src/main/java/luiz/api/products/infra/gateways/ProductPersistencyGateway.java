package luiz.api.products.infra.gateways;

import luiz.api.products.core.entities.Product;
import luiz.api.products.core.enums.ProductStatus;
import luiz.api.products.core.gateways.ProductGateway;
import luiz.api.products.infra.dto.mapper.ProductMapper;
import luiz.api.products.infra.persistency.ProductEntity;
import luiz.api.products.infra.persistency.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductPersistencyGateway implements ProductGateway {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductPersistencyGateway(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Product> getAllProducts(int p, int quantity) {
        Page<ProductEntity> page = repository.findAllProductByStatusEquals(ProductStatus.ACTIVE, PageRequest.of(p, quantity));
        return page.get().map(mapper::toProduct).toList();
    }

    @Override
    public Product saveProduct(Product p) {
        var entity = mapper.toEntity(p);
        return mapper.toProduct(repository.save(entity));
    }

    @Override
    public Optional<Product> getOneProduct(UUID id) {
        var p = repository.findById(id);
        return Optional.ofNullable(mapper.toProduct(p.get()));
    }

    @Override
    public Product updateProduct(Product p) {
        var entity = mapper.toEntity(p);
        return mapper.toProduct(repository.save(entity));
    }

    @Override
    public int deleteProduct(UUID id) {
        return repository.updateProductStatusToInativo(id);
    }
}