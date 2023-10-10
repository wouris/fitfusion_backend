package sk.kasv.mrazik.fitfusion.models.enums.exercises;

import java.util.Arrays;

public enum Equipment {
    ASSISTED("assisted"),
    BAND("band"),
    BARBELL("barbell"),
    BODY_WEIGHT("body weight"),
    BOSU_BALL("bosu ball"),
    CABLE("cable"),
    DUMBBELL("dumbbell"),
    ELLIPTICAL_MACHINE("elliptical machine"),
    EZ_BARBELL("ez barbell"),
    HAMMER("hammer"),
    KETTLEBELL("kettlebell"),
    LEVERAGE_MACHINE("leverage machine"),
    MEDICINE_BALL("medicine ball"),
    OLYMPIC_BARBELL("olympic barbell"),
    RESISTANCE_BAND("resistance band"),
    ROLLER("roller"),
    ROPE("rope"),
    SKIERG_MACHINE("skierg machine"),
    SLED_MACHINE("sled machine"),
    SMITH_MACHINE("smith machine"),
    STABILITY_BALL("stability ball"),
    STATIONARY_BIKE("stationary bike"),
    STEPMILL_MACHINE("stepmill machine"),
    TIRE("tire"),
    TRAP_BAR("trap bar"),
    UPPER_BODY_ERGOMETER("upper body ergometer"),
    WEIGHTED("weighted"),
    WHEEL_ROLLER("wheel roller");

    private final String value;

    Equipment(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(Equipment.values()).anyMatch(equipment -> equipment.value().equals(value));
    }

    public static Equipment fromText(String text) {
        return Arrays.stream(Equipment.values())
                .filter(
                        equipment -> equipment.value().equalsIgnoreCase(text)
                ).findFirst()
                .orElse(null);
    }

    public String value() {
        return value;
    }
}
