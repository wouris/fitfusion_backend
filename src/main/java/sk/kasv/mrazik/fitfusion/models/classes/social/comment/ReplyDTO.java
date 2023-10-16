package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sk.kasv.mrazik.fitfusion.models.serializers.ReplyDTOSerializer;
import sk.kasv.mrazik.fitfusion.utils.TimeUtil;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@JsonSerialize(using = ReplyDTOSerializer.class)
public class ReplyDTO {
    private UUID id;
    private String username;
    private String content;
    private int likes;
    private String createdAgo;

    public ReplyDTO() {
    }

    public ReplyDTO(UUID id, String username, String content, Long likes, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.likes = Optional.ofNullable(likes).orElse(0L).intValue();
        this.createdAgo = TimeUtil.getTimeAgo(createdAt);
    }

    public UUID id() {
        return this.id;
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

    public void id(UUID id) {
        this.id = id;
    }

    public void username(String username) {
        this.username = username;
    }

    public void content(String content) {
        this.content = content;
    }

    public void likes(int likes) {
        this.likes = likes;
    }

    public void createdAgo(String createdAgo) {
        this.createdAgo = createdAgo;
    }
}
