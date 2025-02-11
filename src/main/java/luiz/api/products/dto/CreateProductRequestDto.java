package luiz.api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateProductRequestDto(
        @NotBlank String name,
        @NotNull @Positive Double price,
        List<MultipartFile> imageFile) {
}