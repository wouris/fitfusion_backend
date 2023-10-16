package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentLike;

import java.util.UUID;

public interface CommentLikesRepository extends JpaRepository<CommentLike, UUID> {
    boolean existsByUserIdAndCommentId(UUID id, UUID commentId);

    boolean existsByUserIdAndReplyId(UUID id, UUID replyId);

    int countByCommentId(UUID commentId);

    int countByReplyId(UUID replyId);

    @Modifying
    @Query(value = "DELETE FROM comment_likes WHERE user_id = ?1", nativeQuery = true)
    void deleteById(UUID userId);
}
