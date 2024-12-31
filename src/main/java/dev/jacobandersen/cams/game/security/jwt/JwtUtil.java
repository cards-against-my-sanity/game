package dev.jacobandersen.cams.game.security.jwt;

import dev.jacobandersen.cams.game.config.JwtSecretKeyProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    @Autowired
    public JwtUtil(JwtSecretKeyProperty secretKeyProperty) {
        secretKey = Keys.hmacShaKeyFor(secretKeyProperty.jwtSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractClaims(String token) throws JwtException {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
