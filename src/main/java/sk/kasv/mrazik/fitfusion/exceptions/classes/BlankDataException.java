package sk.kasv.mrazik.fitfusion.exceptions.classes;

public class BlankDataException extends RuntimeException {
    public BlankDataException(String message) {
        super(message);
    }

    public BlankDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
