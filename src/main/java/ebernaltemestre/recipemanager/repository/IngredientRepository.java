package ebernaltemestre.recipemanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ebernaltemestre.recipemanager.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
    Optional<Ingredient> findByName(String name);
}
