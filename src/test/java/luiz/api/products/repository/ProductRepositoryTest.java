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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("Must save a product and return it")
    void saveProductSuccessfully() {
        var product = new Product(UUID.randomUUID(), "Iphone 15", 2000d, ProductStatus.ACTIVE);

        var savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isExactlyInstanceOf(UUID.class);
    }

    @Test
    @DisplayName("Must get one Product successfully")
    void findOneProduct() {
        var product = productRepository.save(new Product(UUID.randomUUID(), "Iphone 14", 2000d, ProductStatus.ACTIVE));

        var result = productRepository.findById(product.getId());

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Must not get any Product")
    void findProductNotExists() {
        var result = productRepository.findById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void getAllProductsWithPagination() {
        List<Product> pageProducts = List.of(
                new Product(UUID.randomUUID(), "teste Service1", 1690d, ProductStatus.ACTIVE),
                new Product(UUID.randomUUID(), "teste Service2", 1690d, ProductStatus.INACTIVE),
                new Product(UUID.randomUUID(), "teste Service3", 1690d, ProductStatus.ACTIVE),
                new Product(UUID.randomUUID(), "teste Service4", 1980d, ProductStatus.INACTIVE)
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
        var product = productRepository.save(new Product(UUID.randomUUID(), "Iphone 14", 2000d, ProductStatus.ACTIVE));

        var deletedProductInteger = productRepository.updateProductStatusToInativo(product.getId());

        assertThat(deletedProductInteger).isEqualTo(1);
    }

    @Test
    void deleteNonExistingProduct() {
        var deletedProductInteger = productRepository.updateProductStatusToInativo(UUID.randomUUID());
        assertThat(deletedProductInteger).isZero();
    }
}