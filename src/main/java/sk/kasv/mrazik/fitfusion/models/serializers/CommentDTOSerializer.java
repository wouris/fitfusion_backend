package sk.kasv.mrazik.fitfusion.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.ReplyDTO;

import java.io.IOException;

public class CommentDTOSerializer extends JsonSerializer<CommentDTO> {

    @Override
    public void serialize(CommentDTO commentDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", commentDTO.id().toString());
        jsonGenerator.writeStringField("postId", commentDTO.postId().toString());
        jsonGenerator.writeStringField("username", commentDTO.username());
        jsonGenerator.writeStringField("content", commentDTO.content());
        jsonGenerator.writeNumberField("likes", commentDTO.likes());
        jsonGenerator.writeStringField("createdAgo", commentDTO.createdAgo());

        jsonGenerator.writeArrayFieldStart("replies");
        for (ReplyDTO reply : commentDTO.replies()){
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", reply.id().toString());
            jsonGenerator.writeStringField("username", reply.username());
            jsonGenerator.writeStringField("content", reply.content());
            jsonGenerator.writeNumberField("likes", reply.likes());
            jsonGenerator.writeStringField("createdAgo", reply.createdAgo());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
