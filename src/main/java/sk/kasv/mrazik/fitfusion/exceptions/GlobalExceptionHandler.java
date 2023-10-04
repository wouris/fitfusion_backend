package sk.kasv.mrazik.fitfusion.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sk.kasv.mrazik.fitfusion.exceptions.classes.*;
import sk.kasv.mrazik.fitfusion.models.classes.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toString());
    }

    @ExceptionHandler(BlankDataException.class)
    public ResponseEntity<String> handleBlankDataException(BlankDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toString());
    }

    @ExceptionHandler(NoRecordException.class)
    public ResponseEntity<String> handleNoRecordException(NoRecordException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()).toString());
    }

    @ExceptionHandler(RecordExistsException.class)
    public ResponseEntity<String> handleRecordExistsException(RecordExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()).toString());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()).toString());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<String> handleInternalServerErrorException(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()).toString());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedActionExceptionException(UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()).toString());
    }
}
