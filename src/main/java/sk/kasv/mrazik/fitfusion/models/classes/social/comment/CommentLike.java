package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.UUID;

@Entity(name = "comment_likes")
@IdClass(CommentLikesId.class)
public class CommentLike {
    @Id
    private UUID userId;
    @Id
    private UUID commentId;
    @Id
    private UUID replyId;

    public CommentLike() {
    }

    public CommentLike(UUID userId, UUID commentId, UUID replyId) {
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

    public UUID replyId() {
        return this.replyId;
    }
}
