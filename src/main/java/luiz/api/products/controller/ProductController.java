package luiz.api.products.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import luiz.api.products.exceptions.RecordNotFoundExt;
import org.springframework.beans.BeanUtils;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new RecordNotFoundExt("Product");
        }
        products.forEach(p -> {
            var id = p.getId();
            p.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        });
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid Product.ProdutoDTO clientproduct) {
        var product = new Product();
        BeanUtils.copyProperties(clientproduct, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOneProduct(@PathVariable UUID id) /*Consegue identificar pelo nome*/ {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional
                .map(product -> {
                    product.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("All products"));
                    return ResponseEntity.status(HttpStatus.OK).body(product);
                })
                .orElseThrow(() -> new RecordNotFoundExt("Product with id " + id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> updateProduct(@PathVariable UUID id, @RequestBody @Valid Product.ProdutoDTO clientProduct) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new RecordNotFoundExt("Product with id " + id);
        }
        var product = productOptional.get();
        BeanUtils.copyProperties(clientProduct, product);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteProduct(@PathVariable UUID id) {
        productRepository.delete(
                productRepository.findById(id).orElseThrow(() -> new RecordNotFoundExt("Product"))
        );
    }
}