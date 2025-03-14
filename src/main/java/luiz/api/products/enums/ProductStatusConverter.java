package luiz.api.products.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import luiz.api.products.exceptions.InvalidEnumEx;

@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus, String> {
    @Override
    public String convertToDatabaseColumn(ProductStatus productStatus) {
        return productStatus.toString();
    }

    @Override
    public ProductStatus convertToEntityAttribute(String s) {
        return switch (s) {
            case "ativo" -> ProductStatus.ACTIVE;
            case "inativo" -> ProductStatus.INACTIVE;
            default -> throw new InvalidEnumEx("ProductStatus", s);
        };
    }
}