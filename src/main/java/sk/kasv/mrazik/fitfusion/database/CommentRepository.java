package sk.kasv.mrazik.fitfusion.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;

import java.util.Set;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // query to return a username instead of a userId
    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO(c.id, c.postId, u.username, c.content, c.likes) FROM comments c JOIN users u ON c.userId = u.id WHERE c.postId = ?1")
    Set<CommentDTO> findAllByPostId(UUID postId);

    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO(c.id, c.postId, u.username, c.content, c.likes) FROM comments c JOIN users u ON c.userId = u.id WHERE c.postId = ?1 ORDER BY c.likes DESC LIMIT 2")
    Set<CommentDTO> findTopByPostId(UUID postId);
}
