package sk.kasv.mrazik.fitfusion.models.classes.user.interactions;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity(name = "follows")
@IdClass(FollowId.class)
public class Follow {
    @Id
    private UUID followerId;

    @Id
    private UUID followingId;

    public Follow() {
    }

    public Follow(UUID followerId, UUID followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public UUID followerId() {
        return this.followerId;
    }

    public void followerId(UUID followerId) {
        this.followerId = followerId;
    }

    public UUID followingId() {
        return this.followingId;
    }

    public void followingId(UUID following_id) {
        this.followingId = following_id;
    }
}
