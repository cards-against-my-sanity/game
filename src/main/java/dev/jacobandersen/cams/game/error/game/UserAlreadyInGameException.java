package dev.jacobandersen.cams.game.error.game;

import dev.jacobandersen.cams.game.security.User;

public class UserAlreadyInGameException extends GameException {
    private final User user;

    public UserAlreadyInGameException(final User user) {
        super(null, String.format("User %s is already in a game, cannot create a new game", user.id()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
