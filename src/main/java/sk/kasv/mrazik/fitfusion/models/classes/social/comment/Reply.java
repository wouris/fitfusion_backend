package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "comment_replies")
public class Reply {
    @Id
    private UUID id;
    private UUID userId;
    private UUID commentId;
    private String content;
    private Timestamp createdAt;

    public Reply() {
    }

    public Reply(UUID userId, UUID commentId, String content, Timestamp createdAt) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public UUID id() {
        return this.id;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID commentId() {
        return this.commentId;
    }

    public String content() {
        return this.content;
    }

    public void id(UUID id) {
        this.id = id;
    }

    public void userId(UUID userId) {
        this.userId = userId;
    }

    public void commentId(UUID commentId) {
        this.commentId = commentId;
    }

    public void content(String content) {
        this.content = content;
    }

    public Timestamp createdAt() {
        return this.createdAt;
    }

    public void createdAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
