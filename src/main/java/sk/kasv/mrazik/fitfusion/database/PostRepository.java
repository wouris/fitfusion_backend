package sk.kasv.mrazik.fitfusion.database;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.kasv.mrazik.fitfusion.models.classes.social.Post;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

}
