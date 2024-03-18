package pl.daveproject.dietapp.recipe.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.daveproject.dietapp.recipe.model.productentry.RecipeProductEntry;
import pl.daveproject.dietapp.security.model.ApplicationUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private RecipeType type;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "recipe",
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<RecipeProductEntry> products = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ApplicationUser applicationUser;
}
