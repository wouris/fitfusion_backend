package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;

import java.util.Set;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query("SELECT NEW sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO(c.id, c.postId, u.username, c.content, cl.likeCount) " +
            "FROM comments c " +
            "JOIN users u ON u.id = c.userId " +
            "LEFT JOIN (SELECT cl.commentId as cID, COUNT(cl) AS likeCount FROM comment_likes cl GROUP BY cl.commentId) cl " +
            "ON c.id = cID " +
            "WHERE c.postId = ?1 " +
            "ORDER BY COALESCE(cl.likeCount, 0) DESC ")
    Set<CommentDTO> findAllByPostId(UUID postId);

    @Query("SELECT NEW sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO(c.id, c.postId, u.username, c.content, cl.likeCount) " +
            "FROM comments c " +
            "JOIN users u ON u.id = c.userId " +
            "LEFT JOIN (SELECT cl.commentId as cID, COUNT(cl) AS likeCount FROM comment_likes cl GROUP BY cl.commentId) cl " +
            "ON c.id = cID " +
            "WHERE c.postId = ?1 " +
            "ORDER BY COALESCE(cl.likeCount, 0) DESC " +
            "LIMIT 2")
    Set<CommentDTO> findTopByPostId(UUID postId);
}
