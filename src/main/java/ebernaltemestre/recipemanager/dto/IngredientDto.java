package ebernaltemestre.recipemanager.dto;

import ebernaltemestre.recipemanager.utils.MeasureType;
import lombok.Data;

@Data
public class IngredientDto {
    private String name;
    private Integer amount;
    private MeasureType measureType;

    public void setName(String newName) {
        this.name = newName.toLowerCase().trim().strip();
    }
}