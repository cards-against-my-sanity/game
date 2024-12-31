package dev.jacobandersen.cams.game.error.game;

import dev.jacobandersen.cams.game.features.game.Game;

public abstract class GameException extends Exception {
    private final Game game;

    public GameException(Game game, String message) {
        super(message);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
