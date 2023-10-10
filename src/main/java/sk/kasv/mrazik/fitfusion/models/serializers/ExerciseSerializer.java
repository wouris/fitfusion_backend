package sk.kasv.mrazik.fitfusion.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import sk.kasv.mrazik.fitfusion.models.classes.Exercise;

import java.io.IOException;
import java.util.List;

public class ExerciseSerializer extends JsonSerializer<Exercise> {

    @Override
    public void serialize(Exercise exercise, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", exercise.id());
        jsonGenerator.writeStringField("name", exercise.name());
        jsonGenerator.writeStringField("bodyPart", exercise.bodyPart());
        jsonGenerator.writeStringField("gifUrl", exercise.gifUrl());
        jsonGenerator.writeStringField("equipment", exercise.equipment());
        jsonGenerator.writeStringField("target", exercise.target());

        jsonGenerator.writeArrayFieldStart("secondaryMuscles");

        List<String> secondaryMuscles = exercise.secondaryMuscles();
        for (String secondaryMuscle : secondaryMuscles) {
            jsonGenerator.writeString(secondaryMuscle);
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.writeArrayFieldStart("instructions");

        List<String> instructions = exercise.instructions();
        for (String instruction : instructions) {
            jsonGenerator.writeString(instruction);
        }

        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
