package dev.jacobandersen.cams.game.security.websocket;

import dev.jacobandersen.cams.game.security.service.TokenService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;

@Controller
public class WsReauthenticationController {
    private final WsSessionManager sessionManager;
    private final TokenService tokenService;

    @Autowired
    public WsReauthenticationController(WsSessionManager sessionManager, TokenService tokenService) {
        this.sessionManager = sessionManager;
        this.tokenService = tokenService;
    }

    @MessageMapping("/reauthenticate")
    public void reauthenticate(Message<String> message) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        final String sessionId = accessor.getSessionId();

        try {
            tokenService.validateToken(message.getPayload());
            sessionManager.setSessionState(sessionId, WsSessionManager.SessionWrapper.SessionState.AUTHENTICATED);
        } catch (JwtException | BadCredentialsException ex) {
            sessionManager.setSessionState(sessionId, WsSessionManager.SessionWrapper.SessionState.FAILED_REAUTHENTICATION);
        }
    }
}
