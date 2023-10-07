package sk.kasv.mrazik.fitfusion.models.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO;

public class PostDTOSerializer extends JsonSerializer<PostDTO> {
    @Override
    public void serialize(PostDTO postDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("image", postDTO.image());
        jsonGenerator.writeStringField("description", postDTO.description());
        jsonGenerator.writeStringField("username", postDTO.username());
        jsonGenerator.writeStringField("createdAgo", postDTO.createdAgo());
        jsonGenerator.writeEndObject();
    }
}
