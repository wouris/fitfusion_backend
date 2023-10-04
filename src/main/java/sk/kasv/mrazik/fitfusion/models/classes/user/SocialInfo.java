package sk.kasv.mrazik.fitfusion.models.classes.user;


public class SocialInfo {
    private String username;
    private int workouts;
    private int followers;
    private int following;

    public SocialInfo(String username, int workouts, int followers, int following) {
        this.username = username;
        this.workouts = workouts;
        this.followers = followers;
        this.following = following;
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
}
