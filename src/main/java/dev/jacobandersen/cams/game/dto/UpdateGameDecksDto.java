package dev.jacobandersen.cams.game.dto;

import java.util.UUID;

public record UpdateGameDecksDto(UUID deckId, boolean active) {
}
