package sk.kasv.mrazik.fitfusion.models.classes;

import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;

/*
 * This class is used to send error response to the client when an exception occurs.
 */
public class ErrorResponse {
    private final ResponseType type;
    private final String message;

    public ErrorResponse(String message) {
        this.type = ResponseType.ERROR;
        this.message = message;
    }

    @Override
    public String toString() {
        return GsonUtil.getInstance().toJson(this);
    }
}

