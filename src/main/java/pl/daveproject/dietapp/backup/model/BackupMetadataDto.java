package pl.daveproject.dietapp.backup.model;

import lombok.Builder;

import java.time.Instant;
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
}
