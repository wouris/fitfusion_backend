package sk.kasv.mrazik.fitfusion.models.classes;

import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;

/*
 * This class is used to send error response to the client when an exception occurs.
 */
public class ErrorResponse {
    private final JsonResponse response;

    public ErrorResponse(String message) {
        this.response = new JsonResponse(ResponseType.ERROR, message);
    }

    @Override
    public String toString() {
        return GsonUtil.getInstance().toJson(response);
    }
}

