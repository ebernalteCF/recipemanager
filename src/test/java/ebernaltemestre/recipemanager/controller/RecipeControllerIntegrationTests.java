package ebernaltemestre.recipemanager.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ebernaltemestre.recipemanager.dto.RecipeDto;
import ebernaltemestre.recipemanager.model.Ingredient;
import ebernaltemestre.recipemanager.model.Recipe;
import ebernaltemestre.recipemanager.service.RecipeService;
import ebernaltemestre.recipemanager.utils.MeasureType;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerIntegrationTests {
    
    @MockBean
    private RecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldCreateRecipe() throws Exception {
        Ingredient ingredient = new Ingredient(1L, "carrot", 5, MeasureType.UNIT);
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient);
        
        Recipe recipe = new Recipe(1L, "test recipe", 4, 20, true, "instructions for the test recipe", ingredientSet);

        mockMvc.perform(post("/recipes").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(recipe)))
        .andExpect(status().isCreated())
        .andDo(print());
    }

    @Test
    void shouldReturnRecipe() throws Exception {
        long id = 1;
        Ingredient ingredient = new Ingredient(id, "carrot", 5, MeasureType.UNIT);
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient);
        
        Recipe recipe = new Recipe(id, "test recipe", 4, 20, true, "instructions for the test recipe", ingredientSet);
        RecipeDto response = convertEntityToDto(recipe);

        when(recipeService.getRecipe(id)).thenReturn(response);
        mockMvc.perform(get("/recipes/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(recipe.getName()))
            .andExpect(jsonPath("$.servings").value(recipe.getServings()))
            .andExpect(jsonPath("$.cookingMinutes").value(recipe.getCookingMinutes()))
            .andExpect(jsonPath("$.vegetarian").value(recipe.isVegetarian()))
            .andExpect(jsonPath("$.instructions").value(recipe.getInstructions()))
            .andDo(print());
    }

    @Test
    void shouldReturnAllRecipes() throws Exception {
        Ingredient ingredient = new Ingredient(1L, "carrot", 5, MeasureType.UNIT);
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient);
        
        Recipe recipe = new Recipe(1L, "test recipe", 4, 20, true, "instructions for the test recipe", ingredientSet);
        Recipe recipe2 = new Recipe(1L, "test recipe", 2, 30, false, "instructions for the test recipe", ingredientSet);

        List<RecipeDto> recipeList = new ArrayList<>();
        recipeList.add(convertEntityToDto(recipe));
        recipeList.add(convertEntityToDto(recipe2));

        when(recipeService.getRecipes()).thenReturn(recipeList);

        mockMvc.perform(get("/recipes"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(recipeList.size())).andReturn();

    }

    @Test
    void shouldReturnEmptyRecipeList() throws Exception {
        long id = 1L;
        when(recipeService.getRecipes()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/recipes/{id}", id)).andExpect(status().isOk())
        .andDo(print());
    }

    private RecipeDto convertEntityToDto(Recipe recipe) {
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);
        RecipeDto recipeDto = new RecipeDto();
        recipeDto = modelMapper.map(recipe, RecipeDto.class);
        return recipeDto;
    }
}
