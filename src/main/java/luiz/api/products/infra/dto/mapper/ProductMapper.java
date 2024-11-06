package luiz.api.products.infra.dto.mapper;

import luiz.api.products.core.entities.Product;
import luiz.api.products.infra.dto.ProductRequestDto;
import luiz.api.products.infra.dto.ProductResponseDto;
import luiz.api.products.infra.persistency.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice());
    }

    public Product dtoToProduct(ProductRequestDto productRecord) {
        return new Product(productRecord.name(), productRecord.price());
    }

    public ProductEntity toEntity(Product product) {
        var productEntity = new ProductEntity(product.getName(), product.getPrice());
        if (product.getId() != null) {
            productEntity.setId(product.getId());
        }
        return productEntity;
    }

    public Product toProduct(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getStatus());
    }
}