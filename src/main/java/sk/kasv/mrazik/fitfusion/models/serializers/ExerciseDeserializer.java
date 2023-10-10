package sk.kasv.mrazik.fitfusion.models.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for instructions and secondary muscles
 */
public class ExerciseDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        List<String> instructions = new ArrayList<>();
        while (jsonParser.nextToken() != null) {
            if (jsonParser.currentToken() == JsonToken.VALUE_STRING) {
                instructions.add(jsonParser.getText());
            }
        }
        return instructions;
    }
}

