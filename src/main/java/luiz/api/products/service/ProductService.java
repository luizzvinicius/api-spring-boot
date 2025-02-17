package luiz.api.products.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import luiz.api.products.dto.CreateProductRequestDto;
import luiz.api.products.dto.ProductDTO;
import luiz.api.products.dto.ProductPageDTO;
import luiz.api.products.dto.UpdateProductDto;
import luiz.api.products.dto.mapper.ProductMapper;
import luiz.api.products.enums.ProductStatus;
import luiz.api.products.exceptions.RecordNotFoundExt;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;
import luiz.api.products.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated // validações do pathvariable funcionarem
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Utils utils;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, Utils utils) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.utils = utils;
    }

    public ProductPageDTO getAllProducts(@PositiveOrZero int pageNumber, @Positive @Max(100) int qtdProducts) {
        Page<Product> page = productRepository.findAllProductByStatusEquals(ProductStatus.ACTIVE, PageRequest.of(pageNumber, qtdProducts));
        if (page.isEmpty()) {
            throw new RecordNotFoundExt("Product");
        }
        List<ProductDTO> productDTOS = page.get().map(productMapper::toDTO).toList();
        return new ProductPageDTO(productDTOS, page.getTotalElements(), page.getTotalPages());
    }

    // Outro controller pode chamar esse método, por isso é interessante manter a validação
    public ProductDTO saveProduct(@Valid CreateProductRequestDto dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new RuntimeException("Product already exists");
        }
        List<String> urls = dto.imageFile().stream().map(file -> utils.uploadImage(file, dto.name())).toList();
        var savedProduct = productRepository.save(new Product(dto.name(), dto.price(), urls));
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO getOneProduct(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundExt("Product with id " + id));
    }

    @Transactional
    public ProductDTO updateProduct(UUID id, @Valid UpdateProductDto dto) {
        return productRepository.findById(id).map(p -> {
            List<String> updatedImages = utils.updateFiles(dto.productImages(), dto.name());
            p.setName(dto.name());
            p.setPrice(dto.price());
            p.setImagesUrl(updatedImages);
            return productMapper.toDTO(productRepository.save(p));
        }).orElseThrow(() -> new RecordNotFoundExt("Product with id " + id));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.findById(id).ifPresentOrElse(
                p -> {
                    productRepository.deleteProductImages(id);
                    utils.deleteFiles(p.getImagesUrl());
                },
                () -> {
                    throw new RecordNotFoundExt("Product with id " + id);
                }
        );

        productRepository.updateProductStatusToInativo(id);
    }
}