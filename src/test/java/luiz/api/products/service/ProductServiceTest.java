package luiz.api.products.service;

import luiz.api.products.dto.ProductDTO;
import luiz.api.products.dto.mapper.ProductMapper;
import luiz.api.products.enums.ProductStatus;
import luiz.api.products.exceptions.RecordNotFoundExt;
import luiz.api.products.model.Product;
import luiz.api.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class) // substitui o openmocks e autoclosesable
class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService service;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void init() {
       productDTO = new ProductDTO(UUID.randomUUID(), "teste Service", 1690d);
       product = new Product(productDTO.id(), "teste Service", 1690d, ProductStatus.ACTIVE);
    }

    @Test
    void productService_saveProduct_returnProductDTO() {
        when(mapper.toEntity(productDTO)).thenReturn(product);
        when(repository.save(any(Product.class))).thenReturn(product);
        when(mapper.toDTO(product)).thenReturn(productDTO);

        var savedProductDTO = service.saveProduct(productDTO);

        assertThat(savedProductDTO).isEqualTo(productDTO);
    }

    @Test
    void productService_getOneProduct_returnProduct() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toDTO(product)).thenReturn(productDTO);

        var serviceFindOneProduct = service.getOneProduct(id);

        assertThat(serviceFindOneProduct).isEqualTo(productDTO);
    }

    @Test
    void productService_getOneProduct_throwsRecordNotFoundExt() {
        var id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOneProduct(id))
                .isInstanceOf(RecordNotFoundExt.class)
                .hasMessageContaining("Product with id " + id);
    }

    // deletion method already tested in repository tests
}