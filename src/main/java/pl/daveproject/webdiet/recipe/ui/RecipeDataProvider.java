package pl.daveproject.webdiet.recipe.ui;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.daveproject.webdiet.recipe.model.RecipeDto;
import pl.daveproject.webdiet.recipe.service.RecipeService;
import pl.daveproject.webdiet.ui.component.Translator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class RecipeDataProvider extends
        AbstractBackEndDataProvider<RecipeDto, RecipeFilter> implements Translator {

    public static final String NAME_SORTING_KEY = "name";
    public static final String TYPE_SORTING_KEY = "type";
    public static final String KCAL_SORTING_KEY = "kcal";

    private final RecipeService recipeService;


    @Override
    protected Stream<RecipeDto> fetchFromBackEnd(Query<RecipeDto, RecipeFilter> query) {
        var recipeStream = recipeService.findAll().stream();
        if (query.getFilter().isPresent()) {
            recipeStream = recipeStream.filter(recipe -> query.getFilter().get().match(recipe));
        }

        if (query.getSortOrders().size() > 0) {
            recipeStream = recipeStream.sorted(sortComparator(query.getSortOrders()));
        }
        return recipeStream.skip(query.getOffset()).limit(query.getLimit());
    }

    @Override
    protected int sizeInBackEnd(Query<RecipeDto, RecipeFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }

    private Comparator<RecipeDto> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream()
                .map(sortOrder -> {
                    var comparator = recipeFieldComparator(sortOrder.getSorted());
                    if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                        comparator = comparator.reversed();
                    }
                    return comparator;
                }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private Comparator<RecipeDto> recipeFieldComparator(String sorted) {
        return switch (sorted) {
            case NAME_SORTING_KEY -> Comparator.comparing(RecipeDto::getName);
            case TYPE_SORTING_KEY ->
                    Comparator.comparing(recipe -> getTranslation(recipe.getType().getTranslationKey()));
            case KCAL_SORTING_KEY -> Comparator.comparing(RecipeDto::getKcal);
            default -> (p1, p2) -> 0;
        };
    }
}
