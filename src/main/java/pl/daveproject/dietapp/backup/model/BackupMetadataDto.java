package pl.daveproject.dietapp.backup.model;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Builder
public record BackupMetadataDto(UUID id,
                                Instant creationDate,
                                Instant lastModifiedDate,
                                int version,
                                byte[] products,
                                byte[] recipes,
                                byte[] shoppingLists,
                                byte[] bmi,
                                byte[] caloricNeeds) {
    public String getCreatedDateAsString() {
        var formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return creationDate == null ? StringUtils.EMPTY : formatter.format(creationDate);
    }
}
