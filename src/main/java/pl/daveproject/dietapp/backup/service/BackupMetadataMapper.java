package pl.daveproject.dietapp.backup.service;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import pl.daveproject.dietapp.backup.model.BackupMetadata;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;

@Mapper(builder = @Builder(disableBuilder = true))
public interface BackupMetadataMapper {

    BackupMetadataDto toDto(BackupMetadata entity);

    BackupMetadata toEntity(BackupMetadataDto dto);
}
