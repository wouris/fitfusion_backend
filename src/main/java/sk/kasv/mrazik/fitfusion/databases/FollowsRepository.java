package sk.kasv.mrazik.fitfusion.databases;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import sk.kasv.mrazik.fitfusion.models.classes.user.interactions.Follow;


public interface FollowsRepository extends JpaRepository<Follow, UUID> {
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    @Modifying
    void deleteByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
}
