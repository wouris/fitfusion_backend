package sk.kasv.mrazik.fitfusion.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.Post;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO;

import java.util.Set;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(value = "SELECT p.* FROM posts p " +
            "INNER JOIN following f ON p.user_id = f.following_id " +
            "WHERE f.user_id = :userId " +
            "AND p.id NOT IN (SELECT l.post_id FROM likes l WHERE l.user_id = :userId) " +
            "ORDER BY p.created_at DESC " +
            "LIMIT :pageSize OFFSET :pageOffset", nativeQuery = true)
    Set<PostDTO> findPostsByFollowing(@Param("userId") UUID userId, @Param("pageSize") int pageSize, @Param("pageOffset") int pageOffset);

    @Query(value = "SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO(p.image, p.description, u.username, p.createdAt) " +
            "FROM posts p " +
            "LEFT JOIN users u ON p.userId = u.id " +
            "WHERE p.userId != ?1 " +
            "AND p.userId NOT IN (SELECT f.followingId FROM following f WHERE f.userId = ?1) " +
            "AND p.id NOT IN (SELECT l.postId FROM likes l WHERE l.userId = ?1) " +
            "AND p.id NOT IN (SELECT p2.id FROM posts p2 " +
            "INNER JOIN following f ON p2.userId = f.followingId " +
            "WHERE f.userId = ?1) " +
            "ORDER BY RAND() " +
            "LIMIT ?2")
    Set<PostDTO> findRandomPosts(UUID userId, int pageSize);

}

