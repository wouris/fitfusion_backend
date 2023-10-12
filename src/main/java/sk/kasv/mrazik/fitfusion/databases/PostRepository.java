package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.Post;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO;

import java.util.Set;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO(p.id, p.image, p.description, u.username, p.createdAt)" +
            "FROM posts p " +
            "INNER JOIN follows f ON p.userId = f.followingId " +
            "LEFT JOIN users u ON p.userId = u.id " +
            "WHERE f.followerId = ?1 " +
            "AND p.id NOT IN (SELECT l.postId FROM likes l WHERE l.userId = ?1) " +
            "ORDER BY p.createdAt DESC " +
            "LIMIT ?2 OFFSET ?3")
    Set<PostDTO> findPostsByFollowing(UUID userId, int pageSize, int pageOffset);

    @Query("SELECT new sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO(p.id, p.image, p.description, u.username, p.createdAt) " +
            "FROM posts p " +
            "LEFT JOIN users u ON p.userId = u.id " +
            "WHERE p.userId != ?1 " +
            "AND p.userId NOT IN (SELECT f.followingId FROM follows f WHERE f.followerId = ?1) " +
           "AND p.id NOT IN (SELECT l.postId FROM likes l WHERE l.userId = ?1) " +
            "AND p.id NOT IN (SELECT p2.id FROM posts p2 " +
            "INNER JOIN follows f ON p2.userId = f.followingId " +
            "WHERE f.followerId = ?1) " +
            "ORDER BY RAND() " +
            "LIMIT ?2")
    Set<PostDTO> findRandomPosts(UUID followerId, int pageSize);

}

