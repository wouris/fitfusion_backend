package sk.kasv.mrazik.fitfusion.models.classes.user.interactions;

import java.io.Serializable;
import java.util.UUID;

public class FollowId implements Serializable {
    private UUID followerId;
    private UUID followingId;

    public FollowId() {
    }

    public FollowId(UUID followerId, UUID followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public UUID followerId() {
        return this.followerId;
    }

    public UUID followingId() {
        return this.followingId;
    }
}
