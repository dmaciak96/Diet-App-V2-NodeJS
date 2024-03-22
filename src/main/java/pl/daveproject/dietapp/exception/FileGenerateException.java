package pl.daveproject.dietapp.exception;

public class FileGenerateException extends RuntimeException {
    public FileGenerateException(String message) {
        super(message);
    }

    public FileGenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}
