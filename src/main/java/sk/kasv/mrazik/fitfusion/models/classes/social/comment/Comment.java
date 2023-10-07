package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "comments")
public class Comment {
    @Id
    private UUID id;
    private UUID userId; // user who commented
    private UUID postId;
    private String content;
    private int likes;

    public Comment() {
    }

    public Comment(UUID userId, UUID postId, String content, int likes) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.likes = likes;
    }

    public UUID id() {
        return this.id;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID postId() {
        return this.postId;
    }

    public String content() {
        return this.content;
    }

    public int likes() {
        return this.likes;
    }

    public void id(UUID id) {
        this.id = id;
    }

    public void userId(UUID userId) {
        this.userId = userId;
    }

    public void postId(UUID postId) {
        this.postId = postId;
    }

    public void content(String content) {
        this.content = content;
    }

    public void likes(int likes) {
        this.likes = likes;
    }
}
