package luiz.api.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import luiz.api.products.dto.ProductDTO;
import luiz.api.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        this.productDTO = new ProductDTO(UUID.randomUUID(), "Test Controller", 500.0);
    }

    @Test
    void productController_saveProduct() throws Exception {
        given(service.saveProduct(ArgumentMatchers.any())).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objMapper.writeValueAsString(productDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void productController_getOneProduct() throws Exception {
        UUID id = productDTO.id();
        when(service.getOneProduct(id)).thenReturn(productDTO);

        ResultActions response = mockMvc.perform(get("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", id.toString())
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void productController_deleteProduct_returnNoContent() throws Exception {
        UUID id = productDTO.id();
        doNothing().when(service).deleteProduct(id);
        ResultActions response = mockMvc.perform(delete("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", id.toString())
        );
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}