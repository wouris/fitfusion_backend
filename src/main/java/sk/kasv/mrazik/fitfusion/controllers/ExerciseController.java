package sk.kasv.mrazik.fitfusion.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InternalServerErrorException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidTokenException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.models.classes.Exercise;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.BodyPart;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.Equipment;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.FilterOptions;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.Target;
import sk.kasv.mrazik.fitfusion.utils.ExerciseReader;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseReader exerciseReader;


    public ExerciseController(ExerciseReader exerciseReader, ObjectMapper mapper) {
        this.exerciseReader = exerciseReader;
    }

    @GetMapping
    public Set<Exercise> allExercises(@RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "value", required = false) String value, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        FilterOptions options = Optional.ofNullable(filter)
                .map(FilterOptions::fromText)
                .orElse(null);

        return Exercise.sort(ExerciseReader.exercises(), options, value);
    }

    @GetMapping("/{id}")
    public Exercise exerciseById(@PathVariable("id") String id, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID uuid) {
        if (TokenUtil.getInstance().isInvalidToken(uuid, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        if (!NumberUtils.isNumber(id)) {
            throw new InternalServerErrorException("ID is not a number!");
        }

        int index = Integer.parseInt(id);
        Exercise exercise = Exercise.findById(ExerciseReader.exercises(), index);

        if (exercise == null) {
            throw new NoRecordException("No exercise found with ID: " + id);
        }

        return exercise;
    }

    @GetMapping("/bodyparts")
    public Set<String> getAllBodyparts(@RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID uuid) {
        if (TokenUtil.getInstance().isInvalidToken(uuid, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        return Arrays
                .stream(BodyPart.values())
                .map(BodyPart::value)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/targets")
    public Set<String> getAllTargets(@RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID uuid) {
        if (TokenUtil.getInstance().isInvalidToken(uuid, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        return Arrays
                .stream(Target.values())
                .map(Target::value)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/equipments")
    public Set<String> getAllEquipments(@RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID uuid) {
        if (TokenUtil.getInstance().isInvalidToken(uuid, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        return Arrays
                .stream(Equipment.values())
                .map(Equipment::value)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
