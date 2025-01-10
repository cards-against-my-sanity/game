package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.config.WsConfig;
import dev.jacobandersen.cams.game.dto.ChangeGameSettingsDto;
import dev.jacobandersen.cams.game.dto.JoinGameDto;
import dev.jacobandersen.cams.game.dto.UpdateGameDecksDto;
import dev.jacobandersen.cams.game.error.game.GameNotFoundException;
import dev.jacobandersen.cams.game.error.game.UserAlreadyInGameException;
import dev.jacobandersen.cams.game.net.response.ResponseType;
import dev.jacobandersen.cams.game.net.response.SocketErrorPayload;
import dev.jacobandersen.cams.game.net.response.SocketResponse;
import dev.jacobandersen.cams.game.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.SocketException;
import java.util.List;
import java.util.UUID;

@Controller
@SendToUser("/queue/reply")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/game/list")
    public SocketResponse<List<Game>> onListGames() {
        return SocketResponse.ok(ResponseType.LIST_GAMES, gameService.listGames());
    }

    @MessageMapping("/game/create")
    public SocketResponse<Game> onCreateGame(User user) {
        try {
            return SocketResponse.ok(ResponseType.CREATE_GAME, gameService.createGame(user));
        } catch (UserAlreadyInGameException ex) {
            return SocketResponse.error(ResponseType.CREATE_GAME, new SocketErrorPayload("Failed to create game", ex.getMessage()));
        }
    }

    @MessageMapping("/game/join")
    public SocketResponse<UUID> onJoinGame(User user, @Payload JoinGameDto dto) {
        try {
            gameService.addUser(dto.gameId(), user, dto.asObserver());
            return SocketResponse.ok(ResponseType.JOIN_GAME, dto.gameId());
        } catch (UserAlreadyInGameException | GameNotFoundException ex) {
            return SocketResponse.error(ResponseType.JOIN_GAME, new SocketErrorPayload("Failed to join game", ex.getMessage()));
        }
    }

    @MessageMapping("/game/leave")
    public SocketResponse<Void> onLeaveGame(User user) {
        gameService.removeUserFromAssociatedGame(user.id());
        return SocketResponse.ok(ResponseType.LEAVE_GAME, null);
    }

    @MessageMapping("/game/{gameId}/updateSettings")
    public SocketResponse<Void> onUpdateSettings(User user, @DestinationVariable UUID gameId, @Payload ChangeGameSettingsDto dto) {
        if (!gameService.isHostOf(user.id(), gameId)) {
            return SocketResponse.error(ResponseType.UPDATE_SETTINGS, new SocketErrorPayload("Failed to change game settings", "Only the host can change the game's settings"));
        }

        gameService.updateGameSettings(gameId, dto);

        return SocketResponse.ok(ResponseType.UPDATE_SETTINGS, null);
    }

    @MessageMapping("/game/{gameId}/updateDecks")
    public SocketResponse<Void> onUpdateDecks(User user, @DestinationVariable UUID gameId, @Payload UpdateGameDecksDto dto) {
        if (!gameService.isHostOf(user.id(), gameId)) {
            return SocketResponse.error(ResponseType.UPDATE_DECKS, new SocketErrorPayload("Failed to update decks", "Only the host can update the decks"));
        }

        gameService.updateGameDecks(gameId, dto);

        return SocketResponse.ok(ResponseType.UPDATE_DECKS, null);
    }
}
