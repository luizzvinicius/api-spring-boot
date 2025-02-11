package luiz.api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateProductDto(
        @NotBlank String name,
        @NotNull double price,
        @NotNull List<MultipartFile> productImages
) {
}
