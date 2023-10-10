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
        String sql = "SELECT " +
                "  u.username AS username, " +
                "  COALESCE(COUNT(DISTINCT w.user_id), 0) AS workout_count, " +
                "  COALESCE(COUNT(DISTINCT f.follower_id), 0) AS follower_count, " +
                "  COALESCE(COUNT(DISTINCT fo.following_id), 0) AS following_count " +
                "FROM users u " +
                "LEFT JOIN workouts w ON u.id = w.user_id " +
                "LEFT JOIN followers f ON u.id = f.user_id " +
                "LEFT JOIN following fo ON u.id = fo.user_id " +
                "WHERE u.id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new SocialInfo(
                        rs.getString("username"),
                        rs.getInt("workout_count"),
                        rs.getInt("follower_count"),
                        rs.getInt("following_count")
                ),
                userId
        );
    }
}
