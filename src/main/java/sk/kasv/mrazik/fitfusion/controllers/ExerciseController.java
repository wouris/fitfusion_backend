package sk.kasv.mrazik.fitfusion.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InternalServerErrorException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidTokenException;
import sk.kasv.mrazik.fitfusion.models.classes.Exercise;
import sk.kasv.mrazik.fitfusion.models.enums.exercises.FilterOptions;
import sk.kasv.mrazik.fitfusion.utils.ExerciseReader;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseReader exerciseReader;
    private final ObjectMapper mapper;

    public ExerciseController(ExerciseReader exerciseReader, ObjectMapper mapper) {
        this.exerciseReader = exerciseReader;
        this.mapper = mapper;
    }

    @GetMapping
    public Set<Exercise> allExercises(@RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "value", required = false) String value, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        FilterOptions options = Optional.ofNullable(filter)
                .map(String::toUpperCase)
                .map(FilterOptions::fromText)
                .orElse(null);

        try {
            Resource resource = exerciseReader.getResource();
            Exercise[] exercises = mapper.readValue(resource.getInputStream(), Exercise[].class);

            return Exercise.sort(exercises, options, value);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException("Error reading exercises from file!");
        }
    }
}
