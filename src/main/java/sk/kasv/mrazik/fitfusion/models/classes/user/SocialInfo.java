package sk.kasv.mrazik.fitfusion.models.classes.user;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sk.kasv.mrazik.fitfusion.models.serializers.SocialInfoSerializer;

@JsonSerialize(using = SocialInfoSerializer.class)
public class SocialInfo {
    private String username;
    private int workouts;
    private int followers;
    private int following;
    private String avatar; // base64

    public SocialInfo(String username, int workouts, int followers, int following, String avatar) {
        this.username = username;
        this.workouts = workouts;
        this.followers = followers;
        this.following = following;
        this.avatar = avatar;
    }

    public String username() {
        return username;
    }

    public void username(String username) {
        this.username = username;
    }

    public int workouts() {
        return workouts;
    }

    public void workouts(int workouts) {
        this.workouts = workouts;
    }

    public int followers() {
        return followers;
    }

    public void followers(int followers) {
        this.followers = followers;
    }

    public int following() {
        return following;
    }

    public void following(int following) {
        this.following = following;
    }

    public void avatar(String avatar) {
        this.avatar = avatar;
    }

    public String avatar() {
        return this.avatar;
    }

    @Override
    public String toString() {
        return "SocialInfo{" +
                "username='" + username + '\'' +
                ", workouts=" + workouts +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}
