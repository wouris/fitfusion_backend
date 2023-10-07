package sk.kasv.mrazik.fitfusion.models.classes.social.comment;


import java.io.Serializable;
import java.util.UUID;

public class CommentLikesId implements Serializable {
    private UUID userId;
    private UUID commentId;

    public CommentLikesId() {
    }

    public CommentLikesId(UUID userId, UUID commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID commentId() {
        return this.commentId;
    }
}
