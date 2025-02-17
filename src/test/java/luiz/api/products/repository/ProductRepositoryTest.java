package luiz.api.products.repository;

import luiz.api.products.enums.ProductStatus;
import luiz.api.products.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {
    private final Product produtoTeste = new Product(
            "Iphone 15", 2000.0, List.of("https://s3.urlaleatoria")
    );

    private final UUID fixedUUID = UUID.randomUUID();
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("Must save a product and return it")
    void saveProductSuccessfully() {
        var savedProduct = productRepository.save(produtoTeste);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(produtoTeste.getId());
    }

    @Test
    @DisplayName("Must get one Product successfully")
    void findOneProduct() {
        productRepository.save(
                new Product(fixedUUID, "Iphone 14", 2000.0, List.of("https://s3.urlaleatoria"), ProductStatus.ACTIVE)
        );

        var result = productRepository.findById(fixedUUID);

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Must not get any Product")
    void findProductNotExists() {
        var result = productRepository.findById(randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void getAllProductsWithPagination() {
        List<String> randomUrl = List.of("https://s3.urlaleatoria");
        List<Product> pageProducts = List.of(
                new Product(randomUUID(), "teste Service1", 1690d, randomUrl, ProductStatus.ACTIVE),
                new Product(randomUUID(), "teste Service2", 1690d, randomUrl, ProductStatus.INACTIVE),
                new Product(randomUUID(), "teste Service3", 1690d, randomUrl, ProductStatus.ACTIVE),
                new Product(randomUUID(), "teste Service4", 1980d, randomUrl, ProductStatus.INACTIVE)
        );
        productRepository.saveAll(pageProducts);
        var pgRequest = PageRequest.of(0, pageProducts.size());
//        Page<Product> page = new PageImpl<>(pageProducts, pgRequest, pageProducts.size());

        Page<Product> findAllProductByStatusEquals = productRepository.findAllProductByStatusEquals(ProductStatus.ACTIVE, pgRequest);

        assertThat(findAllProductByStatusEquals.isEmpty()).isFalse();
        assertThat(findAllProductByStatusEquals.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Must set product status to INACTIVE")
    void deleteExistingProduct() {
        var product = productRepository.save(produtoTeste);

        var deletedProductInteger = productRepository.updateProductStatusToInativo(product.getId());

        assertThat(deletedProductInteger).isEqualTo(1);
    }

    @Test
    void deleteNonExistingProduct() {
        var deletedProductInteger = productRepository.updateProductStatusToInativo(randomUUID());
        assertThat(deletedProductInteger).isZero();
    }
}