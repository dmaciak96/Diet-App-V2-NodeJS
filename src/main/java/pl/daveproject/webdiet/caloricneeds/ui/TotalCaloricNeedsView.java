package pl.daveproject.webdiet.caloricneeds.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import pl.daveproject.webdiet.bmi.model.UnitSystem;
import pl.daveproject.webdiet.caloricneeds.mapper.TotalCaloricNeedsMapper;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsDto;
import pl.daveproject.webdiet.caloricneeds.model.TotalCaloricNeedsRequest;
import pl.daveproject.webdiet.caloricneeds.service.TotalCaloricNeedsService;
import pl.daveproject.webdiet.ui.component.DeleteConfirmDialog;
import pl.daveproject.webdiet.ui.component.grid.CrudGrid;
import pl.daveproject.webdiet.ui.layout.AfterLoginAppLayout;

import java.time.format.DateTimeFormatter;

@PermitAll
@Route(value = "/caloric-needs", layout = AfterLoginAppLayout.class)
public class TotalCaloricNeedsView extends VerticalLayout implements HasDynamicTitle {

    private final CrudGrid<TotalCaloricNeedsDto, TotalCaloricNeedsFilter> totalCaloricNeedsGrid;
    private final TotalCaloricNeedsService totalCaloricNeedsService;
    private final TotalCaloricNeedsMapper totalCaloricNeedsMapper;

    public TotalCaloricNeedsView(TotalCaloricNeedsService totalCaloricNeedsService,
                                 TotalCaloricNeedsDataProvider totalCaloricNeedsDataProvider,
                                 TotalCaloricNeedsFilter totalCaloricNeedsFilter,
                                 TotalCaloricNeedsMapper totalCaloricNeedsMapper) {
        this.totalCaloricNeedsService = totalCaloricNeedsService;
        this.totalCaloricNeedsMapper = totalCaloricNeedsMapper;
        this.totalCaloricNeedsGrid = new CrudGrid<>(totalCaloricNeedsDataProvider, totalCaloricNeedsFilter, false);

        createGridColumns();
        setOnNewClickListener();
        setOnDeleteClickListener();
        add(totalCaloricNeedsGrid);
    }

    private void createGridColumns() {
        totalCaloricNeedsGrid.getGrid()
                .addColumn(e -> e.getAddedDate().format(DateTimeFormatter.ISO_DATE),
                        TotalCaloricNeedsDataProvider.ADDED_DATE_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.added-date"))
                .setSortable(true)
                .setResizable(true);

        totalCaloricNeedsGrid.getGrid()
                .addColumn(e -> e.getValue() + " kcal",
                        TotalCaloricNeedsDataProvider.VALUE_SORTING_KEY)
                .setHeader(getTranslation("total-caloric-needs.value"))
                .setSortable(true)
                .setResizable(true);

        totalCaloricNeedsGrid.getGrid()
                .addColumn(e -> switch (e.getUnit()) {
                            case METRIC -> e.getWeight() + " kg";
                            case IMPERIAL -> e.getWeight() + " lbs";
                        },
                        TotalCaloricNeedsDataProvider.WEIGHT_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.weight"))
                .setSortable(true)
                .setResizable(true);

        totalCaloricNeedsGrid.getGrid()
                .addColumn(e -> switch (e.getUnit()) {
                            case METRIC -> e.getHeight() + " cm";
                            case IMPERIAL -> e.getHeight() + " ft";
                        },
                        TotalCaloricNeedsDataProvider.HEIGHT_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.height"))
                .setSortable(true)
                .setResizable(true);

        totalCaloricNeedsGrid.getGrid()
                .addColumn(TotalCaloricNeedsDto::getAge,
                        TotalCaloricNeedsDataProvider.AGE_SORTING_KEY)
                .setHeader(getTranslation("total-caloric-needs.age"))
                .setSortable(true)
                .setResizable(true);

        totalCaloricNeedsGrid.getGrid()
                .addColumn(e -> getTranslation(e.getActivityLevel().getTranslationKey()),
                        TotalCaloricNeedsDataProvider.ACTIVITY_LEVEL_SORTING_KEY)
                .setHeader(getTranslation("activity-level"))
                .setSortable(true)
                .setResizable(true);

    }

    private void setOnNewClickListener() {
        totalCaloricNeedsGrid.addOnClickListener(event ->
                createAndOpenTotalCaloricNeedsDialog(TotalCaloricNeedsRequest.builder().unit(UnitSystem.METRIC).build()));
    }

    private void createAndOpenTotalCaloricNeedsDialog(TotalCaloricNeedsRequest totalCaloricNeeds) {
        var totalCaloricNeedsDialog = new TotalCaloricNeedsDialog(totalCaloricNeedsService, totalCaloricNeeds, totalCaloricNeedsMapper);
        add(totalCaloricNeedsDialog);
        totalCaloricNeedsDialog.open();
        totalCaloricNeedsDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                totalCaloricNeedsGrid.refresh();
            }
        });
    }

    private void setOnDeleteClickListener() {
        totalCaloricNeedsGrid.deleteOnClickListener(event -> {
            var selected = totalCaloricNeedsGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selected.ifPresent(this::createAndOpenDeleteDialog);
        });
    }

    private void createAndOpenDeleteDialog(TotalCaloricNeedsDto totalCaloricNeeds) {
        var confirmDialog = new DeleteConfirmDialog(getTranslation("delete-dialog.header-total-caloric-needs-suffix"));
        confirmDialog.open();
        confirmDialog.addConfirmListener(event -> {
            totalCaloricNeedsService.delete(totalCaloricNeeds);
            totalCaloricNeedsGrid.refresh();
            confirmDialog.close();
        });
    }

    @Override
    public String getPageTitle() {
        return getTranslation("total-caloric-needs.title");
    }
}
