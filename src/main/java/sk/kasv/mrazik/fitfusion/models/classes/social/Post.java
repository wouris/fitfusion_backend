package sk.kasv.mrazik.fitfusion.models.classes.social;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "posts")
public class Post {

    @Id
    private UUID id;
    private UUID userId; // author
    private String image; // base64
    private String description;

    public Post() {
    }

    public Post(String image, String description, UUID userId) {
        this.id = UUID.randomUUID();
        this.image = image;
        this.description = description;
        this.userId = userId;
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

    public void image(String image) {
        this.image = image;
    }

    public void description(String description) {
        this.description = description;
    }

    public void userId(UUID userId) {
        this.userId = userId;
    }
}
