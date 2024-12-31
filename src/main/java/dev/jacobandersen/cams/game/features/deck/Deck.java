package dev.jacobandersen.cams.game.features.deck;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("decks")
public record Deck(@Id UUID id, String name, String description, int weight) {
}
