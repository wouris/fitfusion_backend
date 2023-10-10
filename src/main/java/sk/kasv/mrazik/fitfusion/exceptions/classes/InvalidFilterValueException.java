package sk.kasv.mrazik.fitfusion.exceptions.classes;

public class InvalidFilterValueException extends RuntimeException {
    public InvalidFilterValueException(String message) {
        super(message);
    }

    public InvalidFilterValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
