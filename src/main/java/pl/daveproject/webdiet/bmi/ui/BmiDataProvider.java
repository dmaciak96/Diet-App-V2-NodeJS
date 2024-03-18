package pl.daveproject.webdiet.bmi.ui;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.daveproject.webdiet.bmi.mapper.BmiMapper;
import pl.daveproject.webdiet.bmi.model.BmiDto;
import pl.daveproject.webdiet.bmi.service.BmiService;
import pl.daveproject.webdiet.ui.component.Translator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BmiDataProvider extends AbstractBackEndDataProvider<BmiDto, BmiFilter> implements Translator {

    public static final String WEIGHT_SORTING_KEY = "weight";
    public static final String HEIGHT_SORTING_KEY = "height";
    public static final String VALUE_SORTING_KEY = "value";
    public static final String ADDED_DATE_SORTING_KEY = "addedDate";
    public static final String RATE_SORTING_KEY = "rate";

    private final BmiService bmiService;
    private final BmiMapper bmiMapper;

    @Override
    protected Stream<BmiDto> fetchFromBackEnd(Query<BmiDto, BmiFilter> query) {
        var stream = bmiService.findAll().stream();
        if (query.getFilter().isPresent()) {
            stream = stream.filter(bmi -> query.getFilter().get().match(bmi));
        }

        if (query.getSortOrders().size() > 0) {
            stream = stream.sorted(sortComparator(query.getSortOrders()));
        }
        return stream.skip(query.getOffset()).limit(query.getLimit());
    }

    @Override
    protected int sizeInBackEnd(Query<BmiDto, BmiFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }

    private Comparator<BmiDto> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream()
                .map(sortOrder -> {
                    var comparator = bmiFieldComparator(sortOrder.getSorted());
                    if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                        comparator = comparator.reversed();
                    }
                    return comparator;
                }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private Comparator<BmiDto> bmiFieldComparator(String sorted) {
        return switch (sorted) {
            case WEIGHT_SORTING_KEY -> Comparator.comparing(BmiDto::getWeight);
            case HEIGHT_SORTING_KEY -> Comparator.comparing(BmiDto::getHeight);
            case VALUE_SORTING_KEY -> Comparator.comparing(BmiDto::getValue);
            case ADDED_DATE_SORTING_KEY -> Comparator.comparing(BmiDto::getAddedDate);
            case RATE_SORTING_KEY -> Comparator.comparing(bmi -> getTranslation(bmi.getRate().getTranslationKey()));
            default -> (p1, p2) -> 0;
        };
    }
}
