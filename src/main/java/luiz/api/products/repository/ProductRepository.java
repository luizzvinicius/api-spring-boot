package luiz.api.products.repository;

import luiz.api.products.enums.ProductStatus;
import luiz.api.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findProductByName(String name);
    List<Product> findAllProductByStatusEquals(ProductStatus s);

    @Modifying
    @Query(value = "UPDATE products SET status = 'inativo' WHERE id = ?1", nativeQuery = true)
    Integer updateProductStatusToInativo(UUID id);
}