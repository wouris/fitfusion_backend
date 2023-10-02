package sk.kasv.mrazik.fitfusion.models.classes.user.auth;

import java.time.LocalDateTime;

public class Token {
    private final String token;
    private final LocalDateTime expires;

    public Token(String token) {
        this.token = token;
        this.expires = LocalDateTime.now().plusHours(12);
    }

    public String token() {
        return this.token;
    }

    public LocalDateTime expires() {
        return this.expires;
    }
}
