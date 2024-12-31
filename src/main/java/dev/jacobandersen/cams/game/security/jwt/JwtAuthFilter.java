package dev.jacobandersen.cams.game.security.jwt;

import dev.jacobandersen.cams.game.security.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

// TODO: not need here, move to Decker
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthenticationService authenticationService;

    public JwtAuthFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = extractToken(request);
        if (token == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticationService.attemptAuthentication(token);

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        final Cookie accessTokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("access_token")).findFirst().orElse(null);

        if (accessTokenCookie != null) {
            return accessTokenCookie.getValue();
        }

        return null;
    }
}
