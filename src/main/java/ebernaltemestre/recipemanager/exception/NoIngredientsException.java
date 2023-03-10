package ebernaltemestre.recipemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoIngredientsException extends RuntimeException{
    public NoIngredientsException(String message) {
        super(message);
    }
}
