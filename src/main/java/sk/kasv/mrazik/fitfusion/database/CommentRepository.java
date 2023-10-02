package sk.kasv.mrazik.fitfusion.database;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.kasv.mrazik.fitfusion.models.classes.social.Comment;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

}
