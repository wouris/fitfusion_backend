package sk.kasv.mrazik.fitfusion.models.classes.social.post;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "posts")
public class Post {

    @Id
    private UUID id;
    private UUID userId; // author
    private String image; // base64
    private String description;
    private Timestamp createdAt;

    public Post() {
    }

    public Post(String image, String description, UUID userId, Timestamp createdAt) {
        this.id = UUID.randomUUID();
        this.image = image;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public UUID id() {
        return this.id;
    }

    public String image() {
        return this.image;
    }

    public String description() {
        return this.description;
    }

    public UUID userId() {
        return this.userId;
    }

    public Timestamp createdAt() {
        return this.createdAt;
    }
}
