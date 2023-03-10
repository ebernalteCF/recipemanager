package ebernaltemestre.recipemanager.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Number of servings are mandatory")
    private Integer servings;

    @NotNull(message = "Cooking time is mandatory")
    @Column(name = "cooking_minutes")
    private Integer cookingMinutes;

    @NotNull(message = "Checking if its vegetarian is mandatory")
    private boolean vegetarian;

    @NotBlank(message = "Adding instructions is mandatory")
    private String instructions;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable( 
        name = "ingredient_recipe_list", 
        joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
        )
    private Set<Ingredient> ingredientSet = new HashSet<>();

    public void addIngredient(Ingredient ingredient) {
        ingredientSet.add(ingredient);
    }
}
