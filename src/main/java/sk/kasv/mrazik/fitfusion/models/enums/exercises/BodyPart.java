package sk.kasv.mrazik.fitfusion.models.enums.exercises;

import java.util.Arrays;

public enum BodyPart {
    BACK("back"),
    CARDIO("cardio"),
    CHEST("chest"),
    LOWER_ARMS("lower arms"),
    LOWER_LEGS("lower legs"),
    NECK("neck"),
    SHOULDERS("shoulders"),
    UPPER_ARMS("upper arms"),
    UPPER_LEGS("upper legs"),
    WAIST("waist");

    private final String value;

    BodyPart(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(BodyPart.values()).anyMatch(bodyPart -> bodyPart.value().equals(value));
    }

    public static BodyPart fromText(String text) {
        return Arrays.stream(BodyPart.values())
                .filter(
                        bodyPart -> bodyPart.value().equalsIgnoreCase(text)
                ).findFirst()
                .orElse(null);
    }

    public String value() {
        return value;
    }
}
