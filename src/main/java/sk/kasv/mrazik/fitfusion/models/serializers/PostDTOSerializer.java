package sk.kasv.mrazik.fitfusion.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO;

import java.io.IOException;

public class PostDTOSerializer extends JsonSerializer<PostDTO> {
    @Override
    public void serialize(PostDTO postDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", postDTO.id().toString());
        jsonGenerator.writeStringField("image", postDTO.image());
        jsonGenerator.writeStringField("description", postDTO.description());
        jsonGenerator.writeStringField("username", postDTO.username());
        jsonGenerator.writeStringField("avatar", postDTO.avatar());
        jsonGenerator.writeStringField("createdAgo", postDTO.createdAgo());
        jsonGenerator.writeNumberField("likes", postDTO.likes());

        jsonGenerator.writeArrayFieldStart("topComments");
        for (CommentDTO comment : postDTO.topComments()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("id", comment.id().toString());
            jsonGenerator.writeStringField("postId", comment.postId().toString());
            jsonGenerator.writeStringField("username", comment.username());
            jsonGenerator.writeStringField("content", comment.content());
            jsonGenerator.writeNumberField("likes", comment.likes());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
