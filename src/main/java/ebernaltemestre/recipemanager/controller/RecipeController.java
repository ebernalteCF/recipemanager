package ebernaltemestre.recipemanager.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ebernaltemestre.recipemanager.dto.RecipeDto;
import ebernaltemestre.recipemanager.model.Recipe;
import ebernaltemestre.recipemanager.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
@Api(value = "", tags={"Recipe Controller"})
@Tag(name = "Recipe Controller", description = "Service to fetch, add, update and delete recipes")
public class RecipeController {
    
    private final RecipeService recipeService;

    @ApiOperation(value = "Get a list of recipes", notes = "Returns a list of recipes")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved"),
        @ApiResponse(code = 404, message = "Recipes not found")
    })
    @GetMapping
    public List<RecipeDto> getRecipes() {
        return recipeService.getRecipes();
    }

    @ApiOperation(value = "Get a recipe", notes = "Returns a recipe by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved"),
        @ApiResponse(code = 404, message = "Recipe not found")
    })
    @GetMapping("{id}")
    public RecipeDto getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @ApiOperation(value = "Create a recipe", notes = "adds a new recipe to the list of recipes in the database")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "The request body was badly formatted")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public RecipeDto createRecipe(@RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @ApiOperation(value = "Updates a recipe", notes = "updates all values from an already existing recipe")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created"),
        @ApiResponse(code = 404, message = "Recipe with certain id not found"),
        @ApiResponse(code = 400, message = "The request body was badly formatted")
    })
    @PutMapping("{id}")
    public RecipeDto updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    // @PatchMapping("{id}")
    // public Recipe partialUpdateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) { 
    //     return recipe;
    // }

    @ApiOperation(value = "Searches a recipe", notes = "searches recipe by checking if it is or not vegetarian, number of servings," + 
    " text match in the instructions, and if it either has or doesnt have certain ingredients")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Search succesful"),
        @ApiResponse(code = 400, message = "The request body was badly formatted")
    })
    @GetMapping("search")
    public List<RecipeDto> searchRecipes(
        @RequestParam(required = false) Boolean isVegetarian,
        @RequestParam(required = false) Integer servings,
        @RequestParam(required = false) String instructionMatch,
        @RequestParam(required = false) String[] withIngredients,
        @RequestParam(required = false) String[] withoutIngredients
    ) {
        return recipeService.searchRecipes(isVegetarian, servings, instructionMatch, withIngredients, withoutIngredients);
    }
    
    @ApiOperation(value = "Deletes a recipe", notes = "deletes an already existing recipe by id")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Recipe deleted"),
        @ApiResponse(code = 404, message = "Recipe with certain id not found")
    })
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteAssignment(id);
    }
}