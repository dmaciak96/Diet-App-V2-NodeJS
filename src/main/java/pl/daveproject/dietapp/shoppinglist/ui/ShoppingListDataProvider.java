package pl.daveproject.dietapp.shoppinglist.ui;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.shoppinglist.model.ShoppingListDto;
import pl.daveproject.dietapp.shoppinglist.service.ShoppingListService;
import pl.daveproject.dietapp.ui.component.Translator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ShoppingListDataProvider extends
        AbstractBackEndDataProvider<ShoppingListDto, ShoppingListFilter> implements Translator {

    public static final String NAME_SORTING_KEY = "name";
    private final ShoppingListService shoppingListService;

    @Override
    protected Stream<ShoppingListDto> fetchFromBackEnd(Query<ShoppingListDto, ShoppingListFilter> query) {
        var stream = shoppingListService.findAll().stream();
        if (query.getFilter().isPresent()) {
            stream = stream.filter(recipe -> query.getFilter().get().match(recipe));
        }

        if (query.getSortOrders().size() > 0) {
            stream = stream.sorted(sortComparator(query.getSortOrders()));
        }
        return stream.skip(query.getOffset()).limit(query.getLimit());
    }

    @Override
    protected int sizeInBackEnd(Query<ShoppingListDto, ShoppingListFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }

    private Comparator<ShoppingListDto> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream()
                .map(sortOrder -> {
                    var comparator = shoppingListFieldComparator(sortOrder.getSorted());
                    if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                        comparator = comparator.reversed();
                    }
                    return comparator;
                }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private Comparator<ShoppingListDto> shoppingListFieldComparator(String sorted) {
        return switch (sorted) {
            case NAME_SORTING_KEY -> Comparator.comparing(ShoppingListDto::getName);
            default -> (p1, p2) -> 0;
        };
    }
}
