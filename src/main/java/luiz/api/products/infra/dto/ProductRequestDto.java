package luiz.api.products.infra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDto(
        @NotBlank String name,
        @NotNull @Positive Double price) {
}