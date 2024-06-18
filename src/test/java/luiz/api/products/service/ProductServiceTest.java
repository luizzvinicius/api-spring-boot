package luiz.api.products.service;

import luiz.api.products.dto.ProductDTO;
import luiz.api.products.dto.mapper.ProductMapper;
import luiz.api.products.enums.ProductStatus;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
// @ExtendWith(MockitoExtension.class) // substitui o openmocks e autoclosesable
class ProductServiceTest {
    private AutoCloseable mockito;
    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper map;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setup() {
        mockito = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockito.close();
    }

    @Test
    @DisplayName("Service save product without problems")
    void saveProduct() {
        var productDTO = new ProductDTO(UUID.randomUUID(), "teste Service", 1690d);
        var product = new Product(productDTO.id(), "teste Service", 1690d, ProductStatus.ACTIVE);

        when(map.toEntity(productDTO)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(map.toDTO(product)).thenReturn(productDTO);

        assertThat(productDTO.id()).isEqualTo(product.getId());
    }
}