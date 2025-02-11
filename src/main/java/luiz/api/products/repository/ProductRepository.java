package luiz.api.products.repository;

import luiz.api.products.enums.ProductStatus;
import luiz.api.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findProductByName(String name);

    Page<Product> findAllProductByStatusEquals(ProductStatus s, Pageable p);

    boolean existsByName(String name);

    @Modifying
    @Query(value = "UPDATE products SET images_url = '{}' WHERE id = ?1", nativeQuery = true)
    void deleteProductImages(UUID id);

    @Modifying
    @Query(value = "UPDATE products SET status = 'inativo' WHERE id = ?1", nativeQuery = true)
    Integer updateProductStatusToInativo(UUID id);
}