package luiz.api.products.controller;

import jakarta.validation.Valid;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    // Onde recebe as solicitações do cliente
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<Object> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("{'message': 'no products avaliable'}");
        }
        products.forEach(p -> {
            var id = p.getId();
            p.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        });
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/product")
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid Product.ProdutoDTO produto) {
        var p = new Product();
        BeanUtils.copyProperties(produto, p);
        System.out.println(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(p));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var product = productOptional.get();
        product.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("All products"));
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid Product.ProdutoDTO produtoClient) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var prod = p.get();
        BeanUtils.copyProperties(produtoClient, prod);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(prod));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted");
    }
}