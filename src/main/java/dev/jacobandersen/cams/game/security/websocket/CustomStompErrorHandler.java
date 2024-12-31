package dev.jacobandersen.cams.game.security.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class CustomStompErrorHandler extends StompSubProtocolErrorHandler {
    private final ApplicationContext applicationContext;

    @Autowired
    public CustomStompErrorHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, @NonNull Throwable throwable) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(clientMessage);

        if (throwable.getCause() instanceof AccessDeniedException && accessor.getUser() != null) {
            applicationContext
                    .getBean(SimpMessagingTemplate.class)
                    .convertAndSendToUser(
                            accessor.getUser().getName(),
                            "/queue/errors",
                            String.format("You are not permitted to do that: %s", throwable.getCause().getMessage())
                    );

            return null;
        }

        return super.handleClientMessageProcessingError(clientMessage, throwable);
    }
}
