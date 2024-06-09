package luiz.api.products.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
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
    // Onde recebe as solicitações do cliente
    // autowired não recomendado
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<Object> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new EntityNotFoundException();
        }
        products.forEach(p -> {
            var id = p.getId();
            p.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        });
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid Product.ProdutoDTO produto) {
        var p = new Product();
        BeanUtils.copyProperties(produto, p);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var product = productOptional.get();
        product.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("All products"));
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid Product.ProdutoDTO produtoClient) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isEmpty()) {
            throw new EntityNotFoundException();
        }
        var prod = p.get();
        BeanUtils.copyProperties(produtoClient, prod);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(prod));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isEmpty()) {
            throw new EntityNotFoundException();
        }
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted");
    }
}