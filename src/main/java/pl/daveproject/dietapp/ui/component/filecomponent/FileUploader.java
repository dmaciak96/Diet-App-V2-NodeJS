package pl.daveproject.dietapp.ui.component.filecomponent;

import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploader extends Upload {

    public FileUploader(Receiver receiver,
                        int maxFileSize,
                        String toManyFilesErrorMessageKey,
                        String fileIsToBigErrorMessageKey,
                        String incorrectFileTypeErrorMessageKey,
                        String... acceptedFileTypes) {
        super(receiver);
        this.setAcceptedFileTypes(acceptedFileTypes);
        this.setMaxFileSize(maxFileSize);
        setupInternationalization(getTranslation(toManyFilesErrorMessageKey),
                getTranslation(fileIsToBigErrorMessageKey),
                getTranslation(incorrectFileTypeErrorMessageKey));
    }

    private void setupInternationalization(String toManyFilesErrorMessage,
                                           String fileIsToBigErrorMessage,
                                           String incorrectFileTypeErrorMessage) {
        var i18N = new UploadI18N();
        i18N.setDropFiles(new UploadI18N.DropFiles().setOne(getTranslation("upload.drop-one-label"))
                .setMany(getTranslation("upload.drop-many-label")));

        i18N.setAddFiles(new UploadI18N.AddFiles().setOne(getTranslation("upload.select-file-button-label"))
                .setMany(getTranslation("upload.select-files-button-label")));

        i18N.setError(new UploadI18N.Error().setTooManyFiles(toManyFilesErrorMessage)
                .setFileIsTooBig(fileIsToBigErrorMessage)
                .setIncorrectFileType(incorrectFileTypeErrorMessage));

        i18N.setUploading(new UploadI18N.Uploading().setStatus(new UploadI18N.Uploading.Status()
                        .setConnecting(getTranslation("upload.connecting"))
                        .setStalled(getTranslation("upload.stalled"))
                        .setProcessing(getTranslation("upload.processing"))
                        .setHeld(getTranslation("upload.held")))
                .setRemainingTime(new UploadI18N.Uploading.RemainingTime()
                        .setPrefix(getTranslation("upload.remaining-time-prefix"))
                        .setUnknown(getTranslation("upload.remaining-time-unknown")))
                .setError(new UploadI18N.Uploading.Error()
                        .setServerUnavailable(getTranslation("upload.server-unavailable-error"))
                        .setUnexpectedServerError(getTranslation("upload.server-unexpected-error"))
                        .setForbidden(getTranslation("upload.forbidden"))));

        this.setI18n(i18N);
    }
}
