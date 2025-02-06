package luiz.api.products.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import luiz.api.products.dto.ProductDTO;
import luiz.api.products.dto.ProductPageDTO;
import luiz.api.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3001", maxAge = 3600)
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPageDTO> getAllProducts(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                         @RequestParam(defaultValue = "20") @Positive @Max(100) int qtdProducts) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts(page, qtdProducts));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody @Valid ProductDTO clientproduct) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.saveProduct(clientproduct));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getOneProduct(@PathVariable UUID id) {
        var product = productService.getOneProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductDTO clientProduct) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(id, clientProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}