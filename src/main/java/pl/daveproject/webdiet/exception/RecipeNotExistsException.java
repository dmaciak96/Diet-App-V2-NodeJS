package pl.daveproject.webdiet.exception;

import java.util.UUID;

public class RecipeNotExistsException extends RuntimeException {
    public RecipeNotExistsException(UUID id) {
        super("Recipe %s not exists in database".formatted(id));
    }
}
