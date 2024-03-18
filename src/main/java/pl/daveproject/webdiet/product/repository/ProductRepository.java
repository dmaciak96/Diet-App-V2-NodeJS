package pl.daveproject.webdiet.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.daveproject.webdiet.product.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByApplicationUserId(UUID applicationUserId);

    Optional<Product> findFirstByApplicationUserIdAndId(UUID userId, UUID id);

    Optional<Product> findFirstByApplicationUserIdAndNameIgnoreCase(UUID userId, String name);
}
