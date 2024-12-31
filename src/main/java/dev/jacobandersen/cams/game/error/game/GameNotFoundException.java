package dev.jacobandersen.cams.game.error.game;

import java.util.UUID;

public class GameNotFoundException extends GameException {
    private final UUID gameId;

    public GameNotFoundException(final UUID gameId) {
        super(null, String.format("Game %s not found", gameId));
        this.gameId = gameId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
