package sk.kasv.mrazik.fitfusion.models.user;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public record User(@Id ObjectId id, String username, String password, Role role) {
}

