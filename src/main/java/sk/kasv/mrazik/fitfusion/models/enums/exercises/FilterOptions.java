package sk.kasv.mrazik.fitfusion.models.enums.exercises;

import java.util.Arrays;

public enum FilterOptions {
    BODY_PART("bodyPart"),
    EQUIPMENT("equipment"),
    TARGET("target");

    private final String value;

    FilterOptions(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(FilterOptions.values()).anyMatch(filter -> filter.value().equals(value));
    }

    public static FilterOptions fromText(String text) {
        return Arrays.stream(FilterOptions.values())
                .filter(
                        option -> option.value().equalsIgnoreCase(text)
                ).findFirst()
                .orElse(null);
    }

    public String value() {
        return value;
    }
}
