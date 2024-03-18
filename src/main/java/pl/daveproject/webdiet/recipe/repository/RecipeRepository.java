package pl.daveproject.webdiet.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.daveproject.webdiet.recipe.model.Recipe;
import pl.daveproject.webdiet.recipe.model.RecipeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    List<Recipe> findAllByApplicationUserId(UUID userId);

    List<Recipe> findAllByApplicationUserIdAndType(UUID userId, RecipeType recipeType);

    Optional<Recipe> findFirstByApplicationUserIdAndId(UUID userId, UUID id);
}
