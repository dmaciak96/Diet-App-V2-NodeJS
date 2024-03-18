package pl.daveproject.dietapp.bmi.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.lang3.StringUtils;
import pl.daveproject.dietapp.bmi.mapper.BmiMapper;
import pl.daveproject.dietapp.bmi.model.BmiDto;
import pl.daveproject.dietapp.bmi.model.BmiRequest;
import pl.daveproject.dietapp.bmi.model.UnitSystem;
import pl.daveproject.dietapp.bmi.service.BmiService;
import pl.daveproject.dietapp.ui.component.DeleteConfirmDialog;
import pl.daveproject.dietapp.ui.component.grid.CrudGrid;
import pl.daveproject.dietapp.ui.layout.AfterLoginAppLayout;

import java.time.format.DateTimeFormatter;

@PermitAll
@Route(value = "/bmi", layout = AfterLoginAppLayout.class)
public class BmiView extends VerticalLayout implements HasDynamicTitle {

    private final CrudGrid<BmiDto, BmiFilter> bmiGrid;
    private final BmiService bmiService;
    private final BmiMapper bmiMapper;

    public BmiView(BmiService bmiService,
                   BmiDataProvider bmiDataProvider,
                   BmiFilter bmiFilter,
                   BmiMapper bmiMapper) {
        this.bmiService = bmiService;
        this.bmiMapper = bmiMapper;
        this.bmiGrid = new CrudGrid<>(bmiDataProvider, bmiFilter, false);

        createGridColumns();
        setOnNewClickListener();
        setOnDeleteClickListener();
        add(bmiGrid);
    }

    private void createGridColumns() {
        bmiGrid.getGrid()
                .addColumn(e -> e.getAddedDate().format(DateTimeFormatter.ISO_DATE),
                        BmiDataProvider.ADDED_DATE_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.added-date"))
                .setSortable(true)
                .setResizable(true);

        bmiGrid.getGrid()
                .addColumn(BmiDto::getValue,
                        BmiDataProvider.VALUE_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.value"))
                .setSortable(true)
                .setResizable(true);

        bmiGrid.getGrid()
                .addColumn(e -> switch (e.getUnit()) {
                            case METRIC -> e.getWeight() + " kg";
                            case IMPERIAL -> e.getWeight() + " lbs";
                        },
                        BmiDataProvider.WEIGHT_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.weight"))
                .setSortable(true)
                .setResizable(true);

        bmiGrid.getGrid()
                .addColumn(e -> switch (e.getUnit()) {
                            case METRIC -> e.getHeight() + " m";
                            case IMPERIAL -> e.getHeight() + " ft";
                        },
                        BmiDataProvider.HEIGHT_SORTING_KEY)
                .setHeader(getTranslation("bmi-view.height"))
                .setSortable(true)
                .setResizable(true);

        bmiGrid.getGrid()
                .addColumn(e -> getTranslation(e.getRate().getTranslationKey()),
                        BmiDataProvider.RATE_SORTING_KEY)
                .setHeader(StringUtils.EMPTY)
                .setSortable(true)
                .setResizable(true);
    }

    private void setOnNewClickListener() {
        bmiGrid.addOnClickListener(event ->
                createAndOpenBmiDialog(BmiRequest.builder().unit(UnitSystem.METRIC).build()));
    }

    private void createAndOpenBmiDialog(BmiRequest bmi) {
        var bmiDialog = new BmiDialog(bmiService, bmi, bmiMapper);
        add(bmiDialog);
        bmiDialog.open();
        bmiDialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                bmiGrid.refresh();
            }
        });
    }

    private void setOnDeleteClickListener() {
        bmiGrid.deleteOnClickListener(event -> {
            var selected = bmiGrid.getGrid()
                    .getSelectedItems()
                    .stream()
                    .findFirst();
            selected.ifPresent(this::createAndOpenDeleteDialog);
        });
    }

    private void createAndOpenDeleteDialog(BmiDto bmi) {
        var confirmDialog = new DeleteConfirmDialog(getTranslation("delete-dialog.header-bmi-suffix"));
        confirmDialog.open();
        confirmDialog.addConfirmListener(event -> {
            bmiService.delete(bmi);
            bmiGrid.refresh();
            confirmDialog.close();
        });
    }

    @Override
    public String getPageTitle() {
        return getTranslation("bmi-view.title");
    }
}
