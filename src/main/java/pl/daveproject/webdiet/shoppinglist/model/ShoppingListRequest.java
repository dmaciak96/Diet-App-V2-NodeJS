package pl.daveproject.webdiet.shoppinglist.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListRequest {

    private UUID id;

    @NotBlank(message = "Shopping list name cannot be blank")
    @Size(min = 1, max = 255, message = "Shopping list name should be between 1 and 255")
    private String name;

    @NotNull(message = "Recipes cannot be null")
    private List<UUID> recipes = new ArrayList<>();
}
