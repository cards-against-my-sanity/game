package dev.jacobandersen.cams.game.dto;

import java.util.UUID;

public record JoinGameDto(UUID gameId, boolean asObserver) {
}
