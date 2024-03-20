package pl.daveproject.dietapp.backup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.repository.BackupMetadataRepository;
import pl.daveproject.dietapp.exception.BackupException;
import pl.daveproject.dietapp.security.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

    private final BackupMetadataMapper backupMetadataMapper;
    private final BackupMetadataRepository backupMetadataRepository;
    private final UserService userService;

    @Override
    public BackupMetadataDto save(BackupMetadataDto backupMetadataDto) {
        var currentUser = userService.getCurrentUser();
        var backupMetadata = backupMetadataMapper.toEntity(backupMetadataDto);
        backupMetadata.setApplicationUser(currentUser);
        var savedEntity = backupMetadataRepository.save(backupMetadata);
        log.debug("Saved new backup metadata {}", savedEntity.getId());
        return backupMetadataMapper.toDto(savedEntity);
    }

    @Override
    public BackupMetadataDto update(UUID id, BackupMetadataDto backupMetadataDto) {
        var currentUser = userService.getCurrentUser();
        var currentEntity = backupMetadataRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        var entityToUpdate = currentEntity.map(e -> {
            var sourceEntity = backupMetadataMapper.toEntity(backupMetadataDto);
            e.setProducts(sourceEntity.getProducts());
            e.setRecipes(sourceEntity.getRecipes());
            e.setShoppingLists(sourceEntity.getShoppingLists());
            e.setBmi(sourceEntity.getBmi());
            e.setCaloricNeeds(sourceEntity.getCaloricNeeds());
            return e;
        }).orElseThrow(() -> new BackupException("Backup metadata not found by id %s".formatted(id)));
        var updatedEntity = backupMetadataRepository.save(entityToUpdate);
        return backupMetadataMapper.toDto(updatedEntity);
    }

    @Override
    public BackupMetadataDto findById(UUID id) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find backup metadata by id {} for user {}", id, currentUser.getEmail());
        var backupMetadata = backupMetadataRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        return backupMetadata.map(backupMetadataMapper::toDto)
                .orElseThrow(() -> new BackupException("Backup metadata not found by id %s".formatted(id)));
    }

    @Override
    public void delete(UUID id) {
        var currentUser = userService.getCurrentUser();
        if (backupMetadataRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id).isPresent()) {
            log.debug("Deleting backup metadata {}", id);
            backupMetadataRepository.deleteById(id);
        }
    }
}
