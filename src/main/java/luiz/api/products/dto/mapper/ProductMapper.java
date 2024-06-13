package luiz.api.products.dto.mapper;

import luiz.api.products.dto.ProductDTO;
import luiz.api.products.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice());
    }

    public Product toEntity(ProductDTO productRecord) {
        var product = new Product();
        BeanUtils.copyProperties(productRecord, product);
        return product;
    }
}