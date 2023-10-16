package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.Reply;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.ReplyDTO;

import java.util.Set;
import java.util.UUID;

public interface ReplyRepository extends JpaRepository<Reply, UUID> {

    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.comment.ReplyDTO(r.id, u.username, r.content, cl.likeCount, r.createdAt) " +
            "FROM comment_replies r " +
            "JOIN users u ON u.id = r.userId " +
            "LEFT JOIN (SELECT cl.replyId as rID, COUNT(cl) AS likeCount FROM comment_likes cl GROUP BY cl.commentId) cl " +
            "ON r.id = rID " +
            "WHERE r.commentId = ?1 " +
            "ORDER BY COALESCE(cl.likeCount, 0) DESC ")
    Set<ReplyDTO> findAllByCommentId(UUID id);

    boolean existsByCommentId(UUID commentId);
}
