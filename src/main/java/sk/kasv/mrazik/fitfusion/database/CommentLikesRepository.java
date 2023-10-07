package sk.kasv.mrazik.fitfusion.database;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentLike;

import java.util.UUID;

public interface CommentLikesRepository extends JpaRepository<CommentLike, UUID> {
    boolean existsByUserIdAndCommentId(UUID id, UUID commentId);

    int countByCommentId(UUID commentId);
}
