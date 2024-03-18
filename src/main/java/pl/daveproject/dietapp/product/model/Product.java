package pl.daveproject.dietapp.product.model;

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
import pl.daveproject.dietapp.product.model.parameter.ProductParameter;
import pl.daveproject.dietapp.security.model.ApplicationUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Double kcal;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product",
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<ProductParameter> parameters = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ApplicationUser applicationUser;
}
