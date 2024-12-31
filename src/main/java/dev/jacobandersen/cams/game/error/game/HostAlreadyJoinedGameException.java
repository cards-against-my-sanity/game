package dev.jacobandersen.cams.game.error.game;

import dev.jacobandersen.cams.game.features.game.Game;
import dev.jacobandersen.cams.game.security.User;

public class HostAlreadyJoinedGameException extends GameException {
    private final User user;

    public HostAlreadyJoinedGameException(final Game game, final User user) {
        super(game, String.format("Game %s already has a host, user %s not accepted", game.getId(), user.id()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
