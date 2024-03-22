package pl.daveproject.dietapp.zip;

import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.exception.FileGenerateException;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipFileCreator implements Closeable {
    private final ZipOutputStream zipOutputStream;
    private final ByteArrayOutputStream byteArrayOutputStream;

    public ZipFileCreator() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        log.debug("Zip output stream was created");
    }

    public void addFileToArchive(String fileName, byte[] data) {
        try {
            log.debug("Try to add {} file to zip archive", fileName);
            var zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(data, 0, data.length);
            zipOutputStream.closeEntry();
            log.info("File {} was successfully added to zip archive", fileName);
        } catch (IOException e) {
            throw new FileGenerateException("ZIP file creation error", e);
        }
    }

    public byte[] generateAsBytesArray() {
        close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void close() {
        try {
            zipOutputStream.close();
            log.debug("Zip output stream was closed successfully");
        } catch (IOException e) {
            throw new FileGenerateException("ZIP file creation error", e);
        }
    }
}
