package pl.daveproject.dietapp.backup.ui;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.service.BackupService;
import pl.daveproject.dietapp.ui.component.Translator;
import pl.daveproject.dietapp.ui.component.grid.GridDataFilter;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BackupDataProvider extends
        AbstractBackEndDataProvider<BackupMetadataDto, GridDataFilter> implements Translator {

    private final BackupService backupService;

    @Override
    protected Stream<BackupMetadataDto> fetchFromBackEnd(Query<BackupMetadataDto, GridDataFilter> query) {
        return backupService.findAll().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<BackupMetadataDto, GridDataFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }
}
