package dev.jacobandersen.cams.game.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.security")
public record JwtSecretKeyProperty(String jwtSecretKey) {
}
