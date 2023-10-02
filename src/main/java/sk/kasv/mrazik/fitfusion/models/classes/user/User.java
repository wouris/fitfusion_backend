package sk.kasv.mrazik.fitfusion.models.classes.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import sk.kasv.mrazik.fitfusion.models.classes.social.Post;
import sk.kasv.mrazik.fitfusion.models.enums.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "users")
public class User {

    @Id
    private UUID id;
    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(UUID id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(UUID id, String username, String password, Role role, Set<Workout> workouts, Set<User> followers, Set<User> following, List<Post> posts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UUID id() {
        return this.id;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    public Role role() {
        return this.role;
    }

    public void id(UUID id) {
        this.id = id;
    }

    public void username(String username) {
        this.username = username;
    }

    public void password(String password) {
        this.password = password;
    }

    public void role(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                " id='" + this.id + "'" +
                ", username='" + this.username + "'" +
                ", password='" + this.password + "'" +
                ", role='" + this.role + "'" +
                "}" + super.toString();
    }
}