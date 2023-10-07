package sk.kasv.mrazik.fitfusion.models.classes.social.post;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import sk.kasv.mrazik.fitfusion.models.serializers.PostDTOSerializer;

@JsonSerialize(using = PostDTOSerializer.class)
public class PostDTO {
    private String image;
    private String description;
    private String username;
    private String createdAgo;

    public PostDTO(){}

    public PostDTO(String image, String description, String username, Timestamp createdAt) {
        this.image = image;
        this.description = description;
        this.username = username;
        this.createdAgo = getTimeAgo(createdAt);
    }

    private static String getTimeAgo(Timestamp createdAt) {
        long now = System.currentTimeMillis();
        long createdAtMillis = createdAt.getTime();
        long diff = now - createdAtMillis;

        long seconds = diff / 1000;
        if (seconds < 60) {
            return seconds + " seconds ago";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes ago";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + " hours ago";
        }

        long days = hours / 24;
        return days + " days ago";
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
}
