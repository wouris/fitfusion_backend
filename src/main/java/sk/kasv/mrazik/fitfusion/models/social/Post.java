package sk.kasv.mrazik.fitfusion.models.social;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "posts")
public class Post {

    @Id
    private UUID id;
    private String image; // base64
    private String description;
    private String author;
    
    public Post() {}

    public Post(String image, String description, String author) {
        this.id = UUID.randomUUID();
        this.image = image;
        this.description = description;
        this.author = author;
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

    public String author() {
        return this.author;
    }

    public void image(String image) {
        this.image = image;
    }

    public void description(String description) {
        this.description = description;
    }

    public void author(String author) {
        this.author = author;
    }
}
