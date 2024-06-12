package luiz.api.products.service;

import jakarta.validation.Valid;
import luiz.api.products.controller.ProductController;
import luiz.api.products.exceptions.RecordNotFoundExt;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Validated
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new RecordNotFoundExt("Product");
        }
        products.forEach(p -> {
            var id = p.getId();
            p.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        });
        return products;
    }

    public Product saveProduct(Product.ProdutoDTO clientproduct) {
        var product = new Product(); // tratar exceção
        BeanUtils.copyProperties(clientproduct, product);
        return productRepository.save(product);
    }

    public Optional<Product> getOneProduct(UUID id) { // TODO: find way to validate UUID
        return productRepository.findById(id);
    }

    @Transactional
    public Product updateProduct(UUID id, @Valid Product.ProdutoDTO clientProduct) {
        Optional<Product> productOptional = this.getOneProduct(id);
        if (productOptional.isEmpty()) {
            throw new RecordNotFoundExt("Product with id " + id);
        }
        var product = productOptional.get();
        BeanUtils.copyProperties(clientProduct, product);
        return productRepository.save(product);
    }

    @Transactional
    public boolean deleteProduct(UUID id) {
        Optional<Product> productOptional = this.getOneProduct(id);
        return productOptional.map(_ -> {
            productRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}