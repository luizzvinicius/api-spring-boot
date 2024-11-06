package luiz.api.products.infra.persistency;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luiz.api.products.core.enums.ProductStatus;
import luiz.api.products.core.enums.ProductStatusConverter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = {@UniqueConstraint(name = "Product Duplicated names", columnNames = {"name"})})
public class ProductEntity extends RepresentationModel<ProductEntity> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double price;

    @Column(length = 10, nullable = false)
    @Convert(converter = ProductStatusConverter.class)
    private ProductStatus status = ProductStatus.ACTIVE;

    public ProductEntity(UUID id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductEntity(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}