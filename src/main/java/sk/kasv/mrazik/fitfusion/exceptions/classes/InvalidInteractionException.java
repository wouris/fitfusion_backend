package sk.kasv.mrazik.fitfusion.exceptions.classes;

public class InvalidInteractionException extends RuntimeException {
    
    public InvalidInteractionException(String message) {
        super(message);
    }

    public InvalidInteractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
