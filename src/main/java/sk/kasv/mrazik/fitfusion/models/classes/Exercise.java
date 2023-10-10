package sk.kasv.mrazik.fitfusion.models.classes;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidFilterValueException;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.BodyPart;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.Equipment;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.FilterOptions;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.Target;
import sk.kasv.mrazik.fitfusion.models.serializers.ExerciseDeserializer;
import sk.kasv.mrazik.fitfusion.models.serializers.ExerciseSerializer;

import java.util.*;
import java.util.stream.Collectors;

@JsonSerialize(using = ExerciseSerializer.class)
public class Exercise {
    private int id;
    private String name;
    private String bodyPart;
    private String gifUrl;
    private String equipment;
    private String target;
    private List<String> secondaryMuscles;
    private List<String> instructions;

    public Exercise(int id, String name, String bodyPart, String gifUrl, String equipment, String target, List<String> secondaryMuscles, List<String> instructions) {
        this.id = id;
        this.name = name;
        this.bodyPart = bodyPart;
        this.gifUrl = gifUrl;
        this.equipment = equipment;
        this.target = target;
        this.secondaryMuscles = secondaryMuscles;
        this.instructions = instructions;
    }

    public static Set<Exercise> sort(Exercise[] exercises, FilterOptions filter, String value) {
        Set<Exercise> exerciseSet = new LinkedHashSet<>(Arrays.asList(exercises));

        if (filter != null) {
            validateFilterValue(filter, value);
            exerciseSet = filterExercises(exerciseSet, filter, value);
        }

        return exerciseSet.stream()
                .sorted(Comparator.comparing(Exercise::name))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static Set<Exercise> filterExercises(Set<Exercise> exercises, FilterOptions filter, String value) {
        return exercises.stream()
                .filter(exercise -> switch (filter) {
                    case BODY_PART -> exercise.bodyPart().equals(value);
                    case EQUIPMENT -> exercise.equipment().equals(value);
                    case TARGET -> exercise.target().equals(value);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static boolean isValidFilterValue(FilterOptions filter, String value) {
        return switch (filter) {
            case BODY_PART -> BodyPart.contains(value);
            case EQUIPMENT -> Equipment.contains(value);
            case TARGET -> Target.contains(value);
        };
    }

    private static void validateFilterValue(FilterOptions filter, String value) {
        if (!isValidFilterValue(filter, value)) {
            throw new InvalidFilterValueException("Invalid filter value: '" + value + "' for filter: '" + filter.value() + "'!");
        }
    }

    public static Exercise findById(Exercise[] exercises, int id) {
        return Arrays.stream(exercises)
                .filter(exercise -> exercise.id() == id)
                .findFirst()
                .orElse(null);
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String bodyPart() {
        return bodyPart;
    }

    public void bodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String gifUrl() {
        return gifUrl;
    }

    public void gifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String equipment() {
        return equipment;
    }

    public void equipment(String equipment) {
        this.equipment = equipment;
    }

    public String target() {
        return target;
    }

    public void target(String target) {
        this.target = target;
    }


    @JsonProperty("secondaryMuscles")
    public List<String> secondaryMuscles() {
        return secondaryMuscles;
    }

    public void secondaryMuscles(List<String> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    @JsonProperty("instructions")
    @JsonDeserialize(using = ExerciseDeserializer.class)
    public List<String> instructions() {
        return instructions;
    }

    public void instructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
