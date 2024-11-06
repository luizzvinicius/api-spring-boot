package luiz.api.products.infra.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import luiz.api.products.infra.dto.ProductPageDto;
import luiz.api.products.infra.dto.ProductRequestDto;
import luiz.api.products.infra.dto.ProductResponseDto;
import luiz.api.products.infra.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPageDto> getAllProducts(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                         @RequestParam(defaultValue = "10") @Positive @Max(50) int qtdProducts) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts(page, qtdProducts));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductRequestDto clientproduct) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.saveProduct(clientproduct));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getOneProduct(@PathVariable UUID id) {
        var product = productService.getOneProduct(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductRequestDto clientProduct) {
        return ResponseEntity.ok(productService.updateProduct(id, clientProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}