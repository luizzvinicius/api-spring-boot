package luiz.api.products.service;

import jakarta.validation.Valid;
import luiz.api.products.dto.ProductDTO;
import luiz.api.products.dto.mapper.ProductMapper;
import luiz.api.products.enums.ProductStatus;
import luiz.api.products.exceptions.RecordNotFoundExt;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAllProductByStatusEquals(ProductStatus.ACTIVE);
        if (products.isEmpty()) {
            throw new RecordNotFoundExt("Product");
        }
        return products.stream().map(productMapper::toDTO).toList();
    }

    // Outro controller pode chamar esse método, por isso é interessante manter a validação
    public ProductDTO saveProduct(@Valid Product product) {
        return productMapper.toDTO(productRepository.save(product));
    }

    public ProductDTO getOneProduct(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundExt("Product with id " + id));
    }

    @Transactional
    public ProductDTO updateProduct(UUID id, @Valid ProductDTO clientProduct) {
        var dtoToProduct = productMapper.toEntity(clientProduct);

        return productRepository.findById(id).map(product -> {
            // BeanUtils.copyProperties(dtoToProduct, product);
            product.setPrice(dtoToProduct.getPrice());
            return productMapper.toDTO(productRepository.save(product));
        }).orElseThrow(() -> new RecordNotFoundExt("Product with id " + id));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.updateProductStatusToInativo(id);
    }
}