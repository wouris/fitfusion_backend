package sk.kasv.mrazik.fitfusion.models.classes.social.comment;


import java.io.Serializable;
import java.util.UUID;

public class CommentLikesId implements Serializable {
    private UUID userId;
    private UUID commentId;
    private UUID replyId;

    public CommentLikesId() {
    }

    public CommentLikesId(UUID userId, UUID commentId, UUID replyId) {
        this.userId = userId;
        this.commentId = commentId;
        this.replyId = replyId;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID commentId() {
        return this.commentId;
    }

    public UUID replyId(){
        return this.replyId;
    }
}
