package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "comments")
public class Comment {
    @Id
    private UUID id;
    private UUID userId; // user who commented
    private UUID postId;
    private String content;
    private Timestamp createdAt;

    public Comment() {
    }

    public Comment(UUID userId, UUID postId, String content) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.postId = postId;
        this.content = content;
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

    public Timestamp createdAt() {
        return this.createdAt;
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

    public void createdAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
