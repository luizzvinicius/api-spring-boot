package luiz.api.products.infra.dto;

import java.util.List;

public record ProductPageDto(
        List<ProductResponseDto> products
) {}