package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.kasv.mrazik.fitfusion.models.classes.social.likes.Like;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    boolean existsByUserIdAndPostId(UUID id, UUID postId);

    @Modifying
    @Query("DELETE FROM likes l WHERE l.userId = ?1 AND l.postId = ?2")
    void deleteByUserIdAndPostId(UUID id, UUID postId);

    int countByPostId(UUID postId);
}
