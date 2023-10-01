package sk.kasv.mrazik.fitfusion.models.social;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "posts")
public class Post {

    @Id
    private UUID id;
    private String image; // base64
    private String description;
    private UUID authorId;

    public Post() {
    }

    public Post(String image, String description, UUID authorId) {
        this.id = UUID.randomUUID();
        this.image = image;
        this.description = description;
        this.authorId = authorId;
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

    public UUID authorId() {
        return this.authorId;
    }

    public void image(String image) {
        this.image = image;
    }

    public void description(String description) {
        this.description = description;
    }

    public void authorId(UUID author) {
        this.authorId = author;
    }
}
