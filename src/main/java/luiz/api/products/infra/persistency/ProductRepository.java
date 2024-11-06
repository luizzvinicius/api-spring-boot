package luiz.api.products.infra.persistency;

import luiz.api.products.core.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Page<ProductEntity> findAllProductByStatusEquals(ProductStatus s, Pageable p);

    @Modifying
    @Query(value = "UPDATE products SET status = 'inativo' WHERE id = ?1", nativeQuery = true)
    Integer updateProductStatusToInativo(UUID id);
}