package pl.daveproject.dietapp.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.daveproject.dietapp.backup.service.BackupConverter;
import pl.daveproject.dietapp.backup.service.BackupService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackupRunnerJsonImpl implements BackupRunner {

    private final BackupService backupService;
    private final BackupConverter backupConverter;

    @Override
    public void startBackup() {

    }
}
