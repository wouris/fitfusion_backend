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

    public CommentLike() {
    }

    public CommentLike(UUID userId, UUID commentId) {
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
