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

@RestController
public class ProductController {
    // Onde recebe as solicitações do cliente
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        System.out.println(products);
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
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
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