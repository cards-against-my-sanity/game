package dev.jacobandersen.cams.game.dto;

import java.util.Optional;

public record ChangeGameSettingsDto(
        Optional<Integer> maxPlayers,
        Optional<Integer> maxObservers,
        Optional<Integer> maxScore,
        Optional<Integer> roundIntermissionTimer,
        Optional<Integer> gameWinIntermissionTimer,
        Optional<Integer> playingTimer,
        Optional<Integer> judgingTimer,
        Optional<Boolean> allowPlayersToJoinMidGame
) {
}
