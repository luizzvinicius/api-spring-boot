package luiz.api.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import luiz.api.products.dto.CreateProductRequestDto;
import luiz.api.products.dto.ProductDTO;
import luiz.api.products.service.ProductService;
import luiz.api.products.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private ProductService service;

    @MockBean
    private Utils utils;

    private ProductDTO productDto;

    @BeforeEach
    void setUp() {
        this.productDto = new ProductDTO(UUID.randomUUID(), "Test Controller", 500.0,
                List.of("https://api-spring-produto.s3.us-east-1.amazonaws.com/ps5/iphone15.webp"));
    }

    @Test
    void productController_saveProduct() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFile",
                "iphone15.png",
                "image/png",
                "fake image content".getBytes()
        );
        var requestDto = new CreateProductRequestDto(productDto.name(), productDto.price(), List.of(mockFile));
        given(utils.isValidImageType(any(MultipartFile.class))).willReturn(true);
        given(service.saveProduct(requestDto)).willReturn(productDto);

        ResultActions response = mockMvc.perform(multipart("/products")
                .file(mockFile)
                .param("name", productDto.name())
                        .param("price", productDto.price().toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Controller"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl[0]").value("https://api-spring-produto.s3.us-east-1.amazonaws.com/ps5/iphone15.webp"));

        verify(utils, times(1)).isValidImageType(any(MultipartFile.class));
        verify(service, times(1)).saveProduct(any(CreateProductRequestDto.class));
    }

    @Test
    void productController_getOneProduct() throws Exception {
        UUID id = productDto.id();
        when(service.getOneProduct(id)).thenReturn(productDto);

        ResultActions response = mockMvc.perform(get("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", id.toString())
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void productController_deleteProduct_returnNoContent() throws Exception {
        UUID id = productDto.id();
        doNothing().when(service).deleteProduct(id);
        ResultActions response = mockMvc.perform(delete("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", id.toString())
        );
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}