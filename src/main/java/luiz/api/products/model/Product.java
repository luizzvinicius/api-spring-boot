package luiz.api.products.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "produtos", uniqueConstraints = {@UniqueConstraint(name= "Product Duplicated names", columnNames = {"name"})})
public class Product extends RepresentationModel<Product> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private Double price;
    private Double imposto;

    public record ProdutoDTO(@NotBlank String name, @NotNull Double price) { }
}