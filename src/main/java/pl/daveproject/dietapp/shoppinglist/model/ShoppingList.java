package pl.daveproject.dietapp.shoppinglist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.daveproject.dietapp.recipe.model.Recipe;
import pl.daveproject.dietapp.security.model.ApplicationUser;
import pl.daveproject.dietapp.shoppinglist.model.productentry.ShoppingListProductEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipe_shopping_list",
            joinColumns = @JoinColumn(name = "shopping_list_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "shoppingList",
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<ShoppingListProductEntry> products = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ApplicationUser applicationUser;
}
