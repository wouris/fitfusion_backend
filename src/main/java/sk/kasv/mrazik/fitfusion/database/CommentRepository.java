package sk.kasv.mrazik.fitfusion.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.kasv.mrazik.fitfusion.models.classes.social.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.social.CommentDTO;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // query to return a username instead of a userId
    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.CommentDTO(c.id, c.postId, u.username, c.content) FROM comments c JOIN users u ON c.userId = u.id WHERE c.postId = ?1")
    List<CommentDTO> findAllByPostId(UUID postId);
}
