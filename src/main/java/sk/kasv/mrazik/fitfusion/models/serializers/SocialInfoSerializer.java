package sk.kasv.mrazik.fitfusion.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import sk.kasv.mrazik.fitfusion.models.classes.user.SocialInfo;

import java.io.IOException;

public class SocialInfoSerializer extends JsonSerializer<SocialInfo> {
    @Override
    public void serialize(SocialInfo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("username", value.username());
        gen.writeNumberField("workouts", value.workouts());
        gen.writeNumberField("followers", value.followers());
        gen.writeNumberField("following", value.following());
        gen.writeStringField("avatar", value.avatar());
        gen.writeEndObject();
    }
}
