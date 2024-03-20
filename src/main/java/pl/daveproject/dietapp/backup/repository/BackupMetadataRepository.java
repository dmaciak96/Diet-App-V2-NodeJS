package pl.daveproject.dietapp.backup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.daveproject.dietapp.backup.model.BackupMetadata;

import java.util.Optional;
import java.util.UUID;

public interface BackupMetadataRepository extends JpaRepository<BackupMetadata, UUID> {
    Optional<BackupMetadata> findFirstByApplicationUserIdAndId(UUID userId, UUID id);
}
