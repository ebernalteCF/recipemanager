package ebernaltemestre.recipemanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ebernaltemestre.recipemanager.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByVegetarian(Boolean vegetarian);
    List<Recipe> findByName(String name);
}