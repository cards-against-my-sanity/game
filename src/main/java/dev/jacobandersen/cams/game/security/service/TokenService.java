package dev.jacobandersen.cams.game.security.service;

import dev.jacobandersen.cams.game.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    private final JwtUtil jwtUtil;

    @Autowired
    public TokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Claims validateToken(String token) {
        final Claims claims = jwtUtil.extractClaims(token);

        if (!claims.get("purpose", String.class).equals("websocket")) {
            throw new BadCredentialsException("Provided token is not a websocket access token!");
        }

        final Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date(System.currentTimeMillis()))) {
            throw new BadCredentialsException("Provided token is expired!");
        }

        return claims;
    }
}
