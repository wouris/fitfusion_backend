package sk.kasv.mrazik.fitfusion.models.enums;

public enum ResponseType {
    SUCCESS("success"),
    ERROR("error");

    private final String type;

    ResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
