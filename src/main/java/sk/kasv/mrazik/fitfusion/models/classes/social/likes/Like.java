package sk.kasv.mrazik.fitfusion.models.classes.social.likes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity(name = "likes")
public class Like {
    @Id
    private UUID userId;
    @Id
    private UUID postId;

    public Like() {
    }

    public Like(UUID userId, UUID postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID postId() {
        return this.postId;
    }
}
