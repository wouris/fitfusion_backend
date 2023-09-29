package sk.kasv.mrazik.fitfusion.utils;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.Id;
import sk.kasv.mrazik.fitfusion.models.user.auth.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenUtil {
    private final Map<UUID, Token> tokens;

    private TokenUtil() {
        this.tokens = new HashMap<>();
    }

    public static String generateToken() {
        return RandomStringUtils.random(40, true, true);
    }

    private static class TokenHolder {
        private static final TokenUtil INSTANCE = new TokenUtil();
    }

    public static TokenUtil getInstance() {
        return TokenHolder.INSTANCE;
    }

    public Token getToken(UUID userId) {
        return this.tokens.get(userId);
    }

    public void addToken(UUID userId, String token) {
        this.tokens.put(userId, new Token(token));
    }

    public void removeToken(UUID userId) {
        this.tokens.remove(userId);
    }

    public boolean isTokenValid(UUID userId, String token) {
        Token userToken = this.tokens.get(userId);
        return userToken != null && userToken.token().equals(token);
    }
}
