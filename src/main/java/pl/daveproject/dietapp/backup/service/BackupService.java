package pl.daveproject.dietapp.backup.service;

import pl.daveproject.dietapp.backup.model.BackupMetadataDto;

import java.util.List;
import java.util.UUID;

public interface BackupService {

    BackupMetadataDto save(BackupMetadataDto backupMetadataDto);

    BackupMetadataDto update(UUID id, BackupMetadataDto backupMetadataDto);

    BackupMetadataDto findById(UUID id);

    List<BackupMetadataDto> findAll();

    void delete(UUID id);

    byte[] generateZipFromBackupData(UUID backupMetadataId);
}
