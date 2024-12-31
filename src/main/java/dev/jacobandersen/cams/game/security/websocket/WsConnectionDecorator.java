package dev.jacobandersen.cams.game.security.websocket;

import dev.jacobandersen.cams.game.features.game.Game;
import dev.jacobandersen.cams.game.features.game.GameService;
import dev.jacobandersen.cams.game.security.User;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class WsConnectionDecorator extends WebSocketHandlerDecorator {
    private final WsSessionManager sessionManager;
    private final GameService gameService;

    public WsConnectionDecorator(WebSocketHandler delegate, WsSessionManager sessionManager, GameService gameService) {
        super(delegate);
        this.sessionManager = sessionManager;
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        sessionManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
        super.afterConnectionClosed(session, closeStatus);

        sessionManager.removeSession(session.getId());

        UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) session.getPrincipal();
        if (principal == null) return;

        User user = (User) principal.getDetails();
        if (user == null) return;

        gameService.removeUserFromAssociatedGame(user.id());
    }
}
