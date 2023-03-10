package ebernaltemestre.recipemanager.dto;

import java.util.Set;

import lombok.Data;

@Data
public class RecipeDto {
    private String name;
    private Integer servings;
    private Integer cookingMinutes;
    private boolean vegetarian;
    private String instructions;
    private Set<IngredientDto> ingredientSet;
}
