package sk.kasv.mrazik.fitfusion.exceptions.classes;

public class RecordExistsException extends RuntimeException {
    public RecordExistsException(String message) {
        super(message);
    }

    public RecordExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
