package sk.kasv.mrazik.fitfusion.models.social;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("posts")
public record Post(String image /* base64 */, String description, String author /* username */) {
    
}
