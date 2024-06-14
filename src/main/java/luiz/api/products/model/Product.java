package luiz.api.products.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luiz.api.products.enums.ProductStatus;
import luiz.api.products.enums.ProductStatusConverter;
import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products", uniqueConstraints = {@UniqueConstraint(name= "Product Duplicated names", columnNames = {"name"})})
// @SQLDelete() sql padrão para todas as chamadas delete
public class Product extends RepresentationModel<Product> implements Serializable {
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
}