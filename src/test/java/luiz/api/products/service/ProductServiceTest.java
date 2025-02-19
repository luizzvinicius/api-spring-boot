package luiz.api.products.service;

import luiz.api.products.bucket.BucketProviderImp;
import luiz.api.products.dto.CreateProductRequestDto;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class) // substitui o openmocks e autoclosesable
class ProductServiceTest {
    private final UUID fixedUUID = UUID.randomUUID();
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private BucketProviderImp bucket;
    @InjectMocks
    private ProductService service;
    private Product product;
    private ProductDTO productDto;

    @BeforeEach
    void init() {
        product = new Product(fixedUUID, "Iphone 15", 2000.0, List.of("https://s3.urlaleatoria"), ProductStatus.ACTIVE);
        productDto = new ProductDTO(fixedUUID, "Iphone 15", 2000.0, List.of("https://s3.urlaleatoria"));
    }

    @Test
    void productService_saveProduct_returnProductDto() {
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "iphone15.png",
                "iphone15/png",
                "fake image content".getBytes()
        );

        when(repository.existsByName(any(String.class))).thenReturn(false);
        when(bucket.uploadImage(any(MultipartFile.class), any(String.class))).thenReturn("https://s3.urlaleatoria");
        when(repository.save(any(Product.class))).thenReturn(product);
        when(mapper.toDTO(product)).thenReturn(productDto);

        var savedProductDto = service.saveProduct(new CreateProductRequestDto(productDto.name(), productDto.price(),
                List.of(mockFile)));

        assertThat(savedProductDto).isEqualTo(productDto); // não vai ser por causa do status
    }

    @Test
    void productService_getOneProduct_returnProduct() {
        when(repository.findById(fixedUUID)).thenReturn(Optional.of(product));
        when(mapper.toDTO(product)).thenReturn(productDto);

        var serviceFindOneProduct = service.getOneProduct(fixedUUID);

        assertThat(serviceFindOneProduct).isEqualTo(productDto);
    }

    @Test
    void productService_getOneProduct_throwsRecordNotFoundExt() {
        when(repository.findById(fixedUUID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOneProduct(fixedUUID))
                .isInstanceOf(RecordNotFoundExt.class)
                .hasMessageContaining("Product with id " + fixedUUID);
    }

    // deletion method already tested in repository tests
}