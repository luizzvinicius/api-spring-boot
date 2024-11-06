package luiz.api.products.infra.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import luiz.api.products.core.exceptions.RecordNotFoundExt;
import luiz.api.products.infra.dto.ProductPageDto;
import luiz.api.products.infra.dto.ProductRequestDto;
import luiz.api.products.infra.dto.ProductResponseDto;
import luiz.api.products.infra.dto.mapper.ProductMapper;
import luiz.api.products.infra.gateways.ProductPersistencyGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class ProductService {
    private final ProductPersistencyGateway gateway;
    private final ProductMapper mapper;

    public ProductService(ProductPersistencyGateway gateway, ProductMapper mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

    public ProductPageDto getAllProducts(@PositiveOrZero int pageNumber, @Positive @Max(100) int qtdProducts) {
        var page = gateway.getAllProducts(pageNumber, qtdProducts);
        if (page.isEmpty()) {
            throw new RecordNotFoundExt("Product");
        }
        var pageFormat = page.stream().map(mapper::toDto).toList();
        return new ProductPageDto(pageFormat);
    }

    public ProductResponseDto saveProduct(@Valid ProductRequestDto dto) {
        var product = mapper.dtoToProduct(dto);
        return mapper.toDto(gateway.saveProduct(
                product
        ));
    }

    public ProductResponseDto getOneProduct(UUID id) {
        return gateway.getOneProduct(id).map(mapper::toDto)
                .orElseThrow(() -> new RecordNotFoundExt("Product with id: " + id));
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID id, @Valid ProductRequestDto clientProduct) {
        var product = mapper.dtoToProduct(clientProduct);

        return gateway.getOneProduct(id).map(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
            return mapper.toDto(gateway.saveProduct(p));
        }).orElseThrow(() -> new RecordNotFoundExt("Product with id: " + id));
    }

    @Transactional
    public int deleteProduct(UUID id) {
        return gateway.deleteProduct(id);
    }
}