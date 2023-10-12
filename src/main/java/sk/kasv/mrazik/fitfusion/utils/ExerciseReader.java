package sk.kasv.mrazik.fitfusion.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.stream.JsonReader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InternalServerErrorException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.models.classes.Exercise;

import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ExerciseReader {

    private static Exercise[] exercises;

    public ExerciseReader(ResourceLoader resourceLoader, ObjectMapper mapper) {
        Resource r = resourceLoader.getResource("classpath:exercises/data.json");

        if (!r.exists()) {
            throw new NoRecordException("No exercise data found!");
        }

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(r.getInputStream()));
            exercises = GsonUtil.getInstance().fromJson(reader, Exercise[].class);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException("Error reading exercises from file!");
        }
    }

    public static Exercise[] exercises() {
        return exercises;
    }
}

