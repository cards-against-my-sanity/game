package dev.jacobandersen.cams.game.dto;

import java.util.List;
import java.util.UUID;

public record UpdateGameDecksDto(List<UUID> deckIds) {
}
