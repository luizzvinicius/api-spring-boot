package luiz.api.products.controller;

import java.util.List;
import java.util.UUID;

import luiz.api.products.exceptions.RecordNotFoundExt;
import luiz.api.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import luiz.api.products.model.Product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid Product.ProdutoDTO clientproduct) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.saveProduct(clientproduct));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOneProduct(@PathVariable UUID id) {
        return productService.getOneProduct(id)
                .map(product -> {
                    product.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("All products"));
                    return ResponseEntity.ok().body(product);
                }).orElseThrow(() -> new RecordNotFoundExt("Product with id " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable UUID id, @RequestBody @Valid Product.ProdutoDTO clientProduct) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(id, clientProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        var deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new RecordNotFoundExt("Product");
        }
        return ResponseEntity.noContent().build();
    }
}