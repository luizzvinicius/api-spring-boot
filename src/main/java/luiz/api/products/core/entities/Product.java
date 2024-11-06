package luiz.api.products.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luiz.api.products.core.enums.ProductStatus;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    private UUID id;
    private String name;
    private Double price;
    private ProductStatus status = ProductStatus.ACTIVE;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}