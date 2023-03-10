package ebernaltemestre.recipemanager.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ebernaltemestre.recipemanager.dto.RecipeDto;
import ebernaltemestre.recipemanager.exception.NoIngredientsException;
import ebernaltemestre.recipemanager.exception.RecipeAlreadyExistsException;
import ebernaltemestre.recipemanager.exception.ResourceNotFoundException;
import ebernaltemestre.recipemanager.model.Ingredient;
import ebernaltemestre.recipemanager.model.Recipe;
import ebernaltemestre.recipemanager.repository.IngredientRepository;
import ebernaltemestre.recipemanager.repository.RecipeRepository;
import ebernaltemestre.recipemanager.utils.ObjectMapperUtils;

@Service
public class RecipeService {
    
    @Autowired
    private ObjectMapperUtils modelMapper;
    
    private final RecipeRepository recipeRepository;

    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public RecipeDto getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Recipe does not exist with id: %d" + id)));
        return convertEntityToDto(recipe);
    }

    public List<RecipeDto> getRecipes() {
        return recipeRepository.findAll()
            .stream()
            .map(this::convertEntityToDto)
            .collect(Collectors.toList());
    }

    public RecipeDto createRecipe(Recipe recipe) {
        recipeExceptionHandler(recipe);
        recipe.setIngredientSet(manageIngredients(recipe));
        recipeRepository.save(recipe);
        return convertEntityToDto(recipe);
    }

    public RecipeDto updateRecipe(Long id, Recipe recipe) {
        recipeExceptionHandler(recipe);
        Recipe originalRecipe = convertDtoToEntity(getRecipe(id));

        originalRecipe.setName(recipe.getName());
        originalRecipe.setServings(recipe.getServings());
        originalRecipe.setCookingMinutes(recipe.getCookingMinutes());
        originalRecipe.setVegetarian(recipe.isVegetarian());
        originalRecipe.setInstructions(recipe.getInstructions());

        Set<Ingredient> newIngredients = determineAddedIngredients(originalRecipe.getIngredientSet(), recipe.getIngredientSet());
        originalRecipe.setIngredientSet(newIngredients);
        recipeRepository.save(originalRecipe);

        return convertEntityToDto(originalRecipe);
    }

    public void deleteAssignment(Long id) {
        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Recipe does not exist with id: %d" + id)));
        recipeRepository.delete(recipe);
    }
    
    public List<RecipeDto> searchRecipes(
        Boolean isVegetarian, Integer servings, String instructionMatch, 
        String[] withIngredients, String[] withoutIngredients) 
    {
        
        List<Recipe> recipeList = isVegetarian == null ?  recipeRepository.findAll() : recipeRepository.findByVegetarian(isVegetarian);

        // Filter by amount of servings 
        if(servings != null) {
            recipeList = recipeList.stream()
            .filter(recipe -> recipe.getServings().equals(servings))
            .collect(Collectors.toList());
        }
        // Filter by contained text in instructions
        if(instructionMatch != null) {
            recipeList = recipeList.stream()
            .filter(recipe -> recipe.getInstructions().contains(instructionMatch))
            .collect(Collectors.toList());
        }
        
        // Filter by unwanted ingredients
        if(withoutIngredients != null){
            for (String ingredient : withoutIngredients) {
                recipeList = recipeList.stream()
                    .filter(recipe -> !recipe.getIngredientSet().iterator().next().getName().equals(ingredient))
                    .collect(Collectors.toList());

            }
        }
    
        // Filter by wanted ingredients
        if(withIngredients != null){
            for (String ingredient : withIngredients) {
                recipeList = recipeList.stream()
                    .filter(recipe -> recipe.getIngredientSet().iterator().next().getName().equals(ingredient))
                    .collect(Collectors.toList());

            }
        }
        return recipeList.stream()
            .map(this::convertEntityToDto)
            .collect(Collectors.toList());
    }

    private RecipeDto convertEntityToDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto = modelMapper.map(recipe, RecipeDto.class);
        return recipeDto;
    }

    private Recipe convertDtoToEntity(RecipeDto recipeDto) {      
        Recipe recipe = new Recipe();
        recipe = modelMapper.map(recipeDto, Recipe.class);
        return recipe;
    }

    /**
     * Handler to controll that recipies are not repeated and contain ingredients
     * 
     * @param recipe
     */
    private void recipeExceptionHandler(Recipe recipe) {
        if(!recipeRepository.findByName(recipe.getName()).isEmpty()) {
            throw new RecipeAlreadyExistsException("This recipe already exists");
        }
        if(recipe.getIngredientSet().isEmpty()) {
            throw new NoIngredientsException("A new recipe must contain ingredients");
        }
    }

    /**
     * Checks which ingredients have to be added inside the recipe when updating.
     * 
     * @param originalIngredients
     * @param newIngredients
     * @return the set of ingredients which have to be added
     */
    private Set<Ingredient> determineAddedIngredients(Set<Ingredient> originalIngredients, Set<Ingredient> newIngredients) {
        Set<Ingredient> ingredientsToAdd = newIngredients.stream()
        .filter(ingredient -> !originalIngredients.contains(ingredient)).collect(Collectors.toSet());
        return ingredientsToAdd;
    }

    /**
     * Changes the name of the ingredients to the right naming format for searching ease.
     * 
     * @param recipe
     * @return A set of ingredients with the right naming format
     */
    private Set<Ingredient> manageIngredients(Recipe recipe) {
        Set<Ingredient> ingredientSet = new HashSet<>();
        if (recipe.getIngredientSet() != null) {
            recipe.getIngredientSet().stream().forEach((ingredient) -> {
                ingredient.setName(ingredient.getName().toLowerCase().trim().strip());
                ingredientRepository.save(ingredient);
                ingredientSet.add(ingredient);
            });
        }
        return ingredientSet;
    }
}
