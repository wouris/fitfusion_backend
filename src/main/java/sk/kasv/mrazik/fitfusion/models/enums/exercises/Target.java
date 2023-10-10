package sk.kasv.mrazik.fitfusion.models.enums.exercises;

import java.util.Arrays;

public enum Target {
    /*
    [
  "abductors",
  "abs",
  "adductors",
  "biceps",
  "calves",
  "cardiovascular system",
  "delts",
  "forearms",
  "glutes",
  "hamstrings",
  "lats",
  "levator scapulae",
  "pectorals",
  "quads",
  "serratus anterior",
  "spine",
  "traps",
  "triceps",
  "upper back"
]
     */
    ABDUCTORS("abductors"),
    ABS("abs"),
    ADDUCTORS("adductors"),
    BICEPS("biceps"),
    CALVES("calves"),
    CARDIOVASCULAR_SYSTEM("cardiovascular system"),
    DELTS("delts"),
    FOREARMS("forearms"),
    GLUTES("glutes"),
    HAMSTRINGS("hamstrings"),
    LATS("lats"),
    LEVATOR_SCAPULAE("levator scapulae"),
    PECTORALS("pectorals"),
    QUADS("quads"),
    SERRATUS_ANTERIOR("serratus anterior"),
    SPINE("spine"),
    TRAPS("traps"),
    TRICEPS("triceps"),
    UPPER_BACK("upper back");

    private final String value;

    Target(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(Target.values()).anyMatch(target -> target.value().equals(value));
    }

    public static Target fromText(String text) {
        return Arrays.stream(Target.values())
                .filter(
                        target -> target.value().equalsIgnoreCase(text)
                ).findFirst()
                .orElse(null);
    }

    public String value() {
        return value;
    }
}
