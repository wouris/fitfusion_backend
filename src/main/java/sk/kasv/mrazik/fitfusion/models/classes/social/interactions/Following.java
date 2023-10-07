package sk.kasv.mrazik.fitfusion.models.classes.social.interactions;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "following")
public class Following {
    @Id
    private UUID userId;

    @Id
    private UUID followingId;

    public Following() {
    }

    public Following(UUID userId, UUID followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID followingId() {
        return this.followingId;
    }
}