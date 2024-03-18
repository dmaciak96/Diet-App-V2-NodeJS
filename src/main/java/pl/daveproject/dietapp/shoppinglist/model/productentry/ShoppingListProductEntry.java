package pl.daveproject.dietapp.shoppinglist.model.productentry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.dietapp.product.model.Product;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingList;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListProductEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double amountInGrams;

    @ManyToOne
    private ShoppingList shoppingList;

    @ManyToOne
    private Product product;
}
