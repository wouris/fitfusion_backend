package sk.kasv.mrazik.fitfusion.utils;

import org.bson.types.ObjectId;
import sk.kasv.mrazik.fitfusion.models.user.auth.Token;

import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    private final Map<ObjectId, Token> tokens;

    private TokenUtil() {
        this.tokens = new HashMap<>();
    }

    private static class TokenHolder {
        private static final TokenUtil INSTANCE = new TokenUtil();
    }

    public static TokenUtil getInstance() {
        return TokenHolder.INSTANCE;
    }

    public Token getToken(ObjectId userId) {
        return this.tokens.get(userId);
    }

    public void addToken(ObjectId userId, String token) {
        this.tokens.put(userId, new Token(token));
    }
}
