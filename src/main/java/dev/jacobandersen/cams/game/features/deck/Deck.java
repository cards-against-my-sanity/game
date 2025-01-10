package dev.jacobandersen.cams.game.features.deck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Deck(UUID id, String name, String description) {
}
