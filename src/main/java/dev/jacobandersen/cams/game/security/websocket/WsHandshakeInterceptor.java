package dev.jacobandersen.cams.game.security.websocket;

import dev.jacobandersen.cams.game.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WsHandshakeInterceptor implements HandshakeInterceptor {
    private final AuthenticationService authenticationService;

    @Autowired
    public WsHandshakeInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {
        UsernamePasswordAuthenticationToken auth = authenticationService.getGuestAuthentication();

        var proto = request.getHeaders().getFirst("Sec-WebSocket-Protocol");
        if (proto != null) {
            for (String protocol : proto.split(",")) {
                protocol = protocol.trim();
                if (protocol.startsWith("Access.")) {
                    try {
                        auth = authenticationService.attemptAuthentication(protocol.substring(7));
                    } catch (Exception ex) {
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return false;
                    }
                }
            }
        }

        attributes.put("auth", auth);
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, Exception exception) {
    }
}
