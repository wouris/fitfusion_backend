package sk.kasv.mrazik.fitfusion.exceptions.classes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sk.kasv.mrazik.fitfusion.models.classes.ErrorResponse;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;

@ControllerAdvice
public class MissingHeaderException extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = GsonUtil.getInstance().toJson(new ErrorResponse(ex.getMessage()));
        return new ResponseEntity<>(error, headers, status);
    }
}
