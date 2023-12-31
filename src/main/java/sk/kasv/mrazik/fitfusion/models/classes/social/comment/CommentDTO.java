package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sk.kasv.mrazik.fitfusion.models.serializers.CommentDTOSerializer;
import sk.kasv.mrazik.fitfusion.utils.TimeUtil;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@JsonSerialize(using = CommentDTOSerializer.class)
public class CommentDTO {
    private final UUID id;
    private final UUID postId;
    private final String username;
    private final String content;
    private int likes;
    private String createdAgo;
    private Set<ReplyDTO> replies;

    public CommentDTO(UUID id, UUID postId, String username, String content, Long likes, Timestamp createdAt) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.likes = Optional.ofNullable(likes).orElse(0L).intValue();
        this.createdAgo = TimeUtil.getTimeAgo(createdAt);
    }

    public UUID id() {
        return this.id;
    }

    public UUID postId() {
        return this.postId;
    }

    public String username() {
        return this.username;
    }

    public String content() {
        return this.content;
    }

    public int likes() {
        return this.likes;
    }

    public String createdAgo() {
        return this.createdAgo;
    }

    public Set<ReplyDTO> replies() {
        return this.replies;
    }

    public void likes(int likes) {
        this.likes = likes;
    }

    public void replies(Set<ReplyDTO> replies) {
        this.replies = replies;
    }
}
