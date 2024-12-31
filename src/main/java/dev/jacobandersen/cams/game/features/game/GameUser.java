package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.security.User;

public record GameUser(User user, GameUserRole role) {
}
