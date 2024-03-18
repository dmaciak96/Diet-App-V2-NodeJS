package pl.daveproject.webdiet.shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.daveproject.webdiet.shoppinglist.model.ShoppingList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID> {

    List<ShoppingList> findAllByApplicationUserId(UUID userId);

    Optional<ShoppingList> findFirstByApplicationUserIdAndId(UUID userId, UUID id);
}
