package luiz.api.products.dto;

import java.util.List;

public record ProductPageDTO(
        List<ProductDTO> products,
        long totalProducts,
        int totalPages
) {
}