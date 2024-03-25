package pl.daveproject.dietapp.ui.component.filecomponent;

import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.ui.component.WebdietNotification;
import pl.daveproject.dietapp.ui.component.type.WebdietNotificationType;

@Slf4j
public class PhotoFileUploader extends FileUploader {
    private static final String JPEG_MIME = "image/jpeg";
    private static final String PNG_MIME = "image/png";
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    public PhotoFileUploader(Receiver receiver) {
        super(receiver, MAX_FILE_SIZE,
                "upload.photo-to-many-files-message",
                "upload.photo-file-rejected",
                "upload.photo-file-rejected",
                JPEG_MIME, PNG_MIME);
        this.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        this.addFileRejectedListener(event -> {
            WebdietNotification.show(getTranslation("upload.photo-file-rejected"),
                    WebdietNotificationType.ERROR);
            log.error("File rejected: {}", event.getErrorMessage());
        });
    }
}
