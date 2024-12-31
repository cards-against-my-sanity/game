package dev.jacobandersen.cams.game.security.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacobandersen.cams.game.error.security.InvalidJwtPurposeException;
import dev.jacobandersen.cams.game.security.User;
import dev.jacobandersen.cams.game.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationService(JwtUtil jwtUtil, ObjectMapper objectMapper, TokenService tokenService) {
        this.objectMapper = objectMapper;
        this.tokenService = tokenService;
    }

    public UsernamePasswordAuthenticationToken getGuestAuthentication() {
        final UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
                .authenticated(
                        "Guest",
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_guest"))
                );

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        return token;
    }

    public UsernamePasswordAuthenticationToken attemptAuthentication(String token) {
        final Claims claims = tokenService.validateToken(token);

        final User user = new User(
                UUID.fromString(claims.getSubject()),
                claims.get("nickname", String.class),
                objectMapper.convertValue(claims.get("roles"), new TypeReference<>() {
                })
        );

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.nickname(), user, user.getAuthorities());
        authToken.setDetails(user);

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authToken);
        SecurityContextHolder.setContext(securityContext);

        return authToken;
    }
}
