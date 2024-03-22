package pl.daveproject.dietapp.backup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.dietapp.backup.model.BackupFileNames;
import pl.daveproject.dietapp.backup.model.BackupMetadataDto;
import pl.daveproject.dietapp.backup.repository.BackupMetadataRepository;
import pl.daveproject.dietapp.exception.BackupException;
import pl.daveproject.dietapp.exception.FileGenerateException;
import pl.daveproject.dietapp.security.service.UserService;
import pl.daveproject.dietapp.zip.ZipFileCreator;

import java.time.Instant;
import java.util.List;
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
        if (backupMetadata.getCreationDate() == null) {
            backupMetadata.setCreationDate(Instant.now());
        }
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
            e.setProducts(sourceEntity.getProducts() == null ? e.getProducts() : sourceEntity.getProducts());
            e.setRecipes(sourceEntity.getRecipes() == null ? e.getRecipes() : sourceEntity.getRecipes());
            e.setShoppingLists(sourceEntity.getShoppingLists() == null ? e.getShoppingLists() : sourceEntity.getShoppingLists());
            e.setBmi(sourceEntity.getBmi() == null ? e.getBmi() : sourceEntity.getBmi());
            e.setCaloricNeeds(sourceEntity.getCaloricNeeds() == null ? e.getCaloricNeeds() : sourceEntity.getCaloricNeeds());
            e.setLastModifiedDate(Instant.now());
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
    public List<BackupMetadataDto> findAll() {
        var currentUser = userService.getCurrentUser();
        log.debug("Find all backup metadata for user {}", currentUser.getEmail());
        var entities = backupMetadataRepository.findAllByApplicationUserId(currentUser.getId());
        log.debug("Mapping {} entities to dto", entities.size());
        return backupMetadataMapper.toDtoList(entities);
    }

    @Override
    public void delete(UUID id) {
        var currentUser = userService.getCurrentUser();
        if (backupMetadataRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id).isPresent()) {
            log.debug("Deleting backup metadata {}", id);
            backupMetadataRepository.deleteById(id);
        }
    }

    @Override
    public byte[] generateZipFromBackupData(UUID backupMetadataId) {
        var metadataDto = findById(backupMetadataId);
        var zipFileName = "backup_%s.zip".formatted(metadataDto.getCreatedDateAsStringWithoutSpace());

        checkIfBackupDataIsPresent(metadataDto.products(), "Products");
        checkIfBackupDataIsPresent(metadataDto.recipes(), "Recipes");
        checkIfBackupDataIsPresent(metadataDto.shoppingLists(), "Shopping lists");
        checkIfBackupDataIsPresent(metadataDto.bmi(), "BMI");
        checkIfBackupDataIsPresent(metadataDto.caloricNeeds(), "Total caloric needs");

        try (var zipFileCreator = new ZipFileCreator()) {
            zipFileCreator.addFileToArchive(BackupFileNames.PRODUCTS.getFileName(), metadataDto.products());
            zipFileCreator.addFileToArchive(BackupFileNames.RECIPES.getFileName(), metadataDto.recipes());
            zipFileCreator.addFileToArchive(BackupFileNames.SHOPPING_LISTS.getFileName(), metadataDto.shoppingLists());
            zipFileCreator.addFileToArchive(BackupFileNames.BMI.getFileName(), metadataDto.bmi());
            zipFileCreator.addFileToArchive(BackupFileNames.TOTAL_CALORIC_NEEDS.getFileName(), metadataDto.caloricNeeds());
            return zipFileCreator.generateAsBytesArray();
        }
    }

    private void checkIfBackupDataIsPresent(byte[] backupData, String backupDataName) {
        if (backupData == null) {
            throw new FileGenerateException("%s backup data is empty".formatted(backupDataName));
        }
    }
}
