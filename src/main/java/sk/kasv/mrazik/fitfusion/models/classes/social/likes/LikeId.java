package sk.kasv.mrazik.fitfusion.models.classes.social.likes;

import java.io.Serializable;
import java.util.UUID;

public class LikeId implements Serializable {
    private UUID userId;
    private UUID postId;

    public LikeId() {
    }

    public LikeId(UUID userId, UUID postId) {
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

