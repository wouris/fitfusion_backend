package sk.kasv.mrazik.fitfusion.models.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import sk.kasv.mrazik.fitfusion.models.classes.social.comment.ReplyDTO;

public class ReplyDTOSerializer extends JsonSerializer<ReplyDTO> {

    @Override
    public void serialize(ReplyDTO replyDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", replyDTO.id().toString());
        jsonGenerator.writeStringField("username", replyDTO.username());
        jsonGenerator.writeStringField("content", replyDTO.content());
        jsonGenerator.writeNumberField("likes", replyDTO.likes());
        jsonGenerator.writeStringField("createdAgo", replyDTO.createdAgo());
        jsonGenerator.writeEndObject();
    }
}
