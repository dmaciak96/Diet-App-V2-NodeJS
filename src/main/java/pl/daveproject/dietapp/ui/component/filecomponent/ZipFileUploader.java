package pl.daveproject.dietapp.ui.component.filecomponent;

import com.vaadin.flow.component.upload.Receiver;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.ui.component.WebdietNotification;
import pl.daveproject.dietapp.ui.component.type.WebdietNotificationType;

@Slf4j
public class ZipFileUploader extends FileUploader {
    private static final int MAX_ZIP_FILE_SIZE = 1073741824; //1 Gib
    private static final String[] ACCEPTED_FILE_TYPES = {"application/zip", "application/x-zip", "application/x-zip-compressed", "multipart/x-zip"};

    public ZipFileUploader(Receiver receiver) {
        super(receiver,
                MAX_ZIP_FILE_SIZE,
                "upload.zip-to-many-files-message",
                "upload.zip-file-rejected",
                "upload.zip-file-rejected",
                ACCEPTED_FILE_TYPES);

        this.addFileRejectedListener(event -> {
            WebdietNotification.show(getTranslation("upload.zip-file-rejected"),
                    WebdietNotificationType.ERROR);
            log.error("File rejected: {}", event.getErrorMessage());
        });
    }
}
