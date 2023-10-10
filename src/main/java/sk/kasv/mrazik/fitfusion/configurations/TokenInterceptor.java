package sk.kasv.mrazik.fitfusion.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidTokenException;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.util.UUID;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().startsWith("/auth/login") || request.getRequestURI().startsWith("/auth/register")) {
            return true;
        }

        String id = request.getHeader("USER_ID");
        String token = request.getHeader("Authorization");

        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        if (TokenUtil.getInstance().isInvalidToken(uuid, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        return true;
    }
}

