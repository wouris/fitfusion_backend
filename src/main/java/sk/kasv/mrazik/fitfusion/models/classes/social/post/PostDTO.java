package sk.kasv.mrazik.fitfusion.models.classes.social.post;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;
import sk.kasv.mrazik.fitfusion.models.serializers.PostDTOSerializer;
import sk.kasv.mrazik.fitfusion.utils.TimeUtil;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@JsonSerialize(using = PostDTOSerializer.class)
public class PostDTO {
    private UUID id;
    private String image;
    private String description;
    private String username;
    private String createdAgo;
    private Set<CommentDTO> topComments;
    private int likes;

    public PostDTO() {
    }

    public PostDTO(UUID id, String image, String description, String username, Timestamp createdAt) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.username = username;
        this.createdAgo = TimeUtil.getTimeAgo(createdAt);
    }

    public PostDTO(UUID id, String image, String description, String username, String createdAgo, Set<CommentDTO> topComments, int likes) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.username = username;
        this.createdAgo = createdAgo;
        this.topComments = topComments;
        this.likes = likes;
    }

    public UUID id() {
        return id;
    }

    public String image() {
        return image;
    }

    public String description() {
        return description;
    }

    public String username() {
        return username;
    }

    public String createdAgo() {
        return createdAgo;
    }

    public int likes() {
        return likes;
    }

    public int likes(int likes) {
        return this.likes = likes;
    }

    public Set<CommentDTO> topComments() {
        return topComments;
    }

    public void topComments(Set<CommentDTO> topComments) {
        this.topComments = topComments;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", image='" + "BASE64" + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", createdAgo='" + createdAgo + '\'' +
                ", likes=" + likes +
                ", topComments=" + topComments +
                '}';
    }
}
