package sk.kasv.mrazik.fitfusion.models.classes.social.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sk.kasv.mrazik.fitfusion.models.serializers.CommentDTOSerializer;

import java.util.UUID;

@JsonSerialize(using = CommentDTOSerializer.class)
public class CommentDTO {
    // UUID id, UUID postId, String username, String content, int likes
    private final UUID id;
    private final UUID postId;
    private final String username;
    private final String content;
    private int likes;

    public CommentDTO(UUID id, UUID postId, String username, String content) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.likes = 0;
    }

    public CommentDTO(UUID id, UUID postId, String username, String content, int likes) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.likes = likes;
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

    public void likes(int likes) {
        this.likes = likes;
    }
}
