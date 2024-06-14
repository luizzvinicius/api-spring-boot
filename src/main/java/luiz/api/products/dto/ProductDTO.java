package luiz.api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ProductDTO(
        UUID id,
        @NotBlank String name,
        @NotNull @Positive Double price) {
}