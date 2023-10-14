package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentLike;

import java.util.UUID;

public interface CommentLikesRepository extends JpaRepository<CommentLike, UUID> {
    boolean existsByUserIdAndCommentId(UUID id, UUID commentId);

    int countByCommentId(UUID commentId);

    @Modifying
    @Query(value = "DELETE FROM comment_likes WHERE user_id = ?1", nativeQuery = true)
    void deleteById(UUID userId);
}
