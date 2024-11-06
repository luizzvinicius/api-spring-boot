package luiz.api.products.infra.dto;

import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        Double price
) {}