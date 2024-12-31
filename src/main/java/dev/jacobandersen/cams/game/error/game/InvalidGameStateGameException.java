package dev.jacobandersen.cams.game.error.game;

import dev.jacobandersen.cams.game.features.game.Game;
import dev.jacobandersen.cams.game.features.game.GameState;

public class InvalidGameStateGameException extends GameException {
    private final GameState state;

    public InvalidGameStateGameException(final Game game, final GameState state) {
        super(game, String.format("Invalid game state %s for action", state));
        this.state = state;
    }

    public GameState getState() {
        return state;
    }
}
