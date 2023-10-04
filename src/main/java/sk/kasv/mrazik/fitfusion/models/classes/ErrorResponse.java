package sk.kasv.mrazik.fitfusion.models.classes;

import sk.kasv.mrazik.fitfusion.utils.GsonUtil;

/*
 * This class is used to send error response to the client when an exception occurs.
 */
public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int status() {
        return status;
    }

    public void status(int status) {
        this.status = status;
    }

    public String message() {
        return message;
    }

    public void message(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return GsonUtil.getInstance().toJson(this);
    }
}

