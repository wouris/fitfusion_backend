package sk.kasv.mrazik.fitfusion.databases;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sk.kasv.mrazik.fitfusion.models.classes.user.SocialInfo;

import java.util.UUID;

@Repository
public class SocialInfoRepository {

    private final JdbcTemplate jdbcTemplate;

    public SocialInfoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SocialInfo getSocialInfo(UUID userId) {
        String id = userId.toString();
        String sql = "SELECT " +
                "  u.username AS username, " +
                "  u.avatar AS avatar, " +
                "  (SELECT COUNT(*) FROM workouts w WHERE w.user_id = ?) AS workout_count, " +
                "  (SELECT COUNT(*) FROM follows f WHERE f.following_id = ?) AS follower_count, " +
                "  (SELECT COUNT(*) FROM follows f WHERE f.follower_id = ?) AS following_count " +
                "  FROM users u" +
                "  WHERE u.id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new SocialInfo(
                        rs.getString("username"),
                        rs.getInt("workout_count"),
                        rs.getInt("follower_count"),
                        rs.getInt("following_count"),
                        rs.getString("avatar")
                ),
                id, id, id, id
        );
    }
}
