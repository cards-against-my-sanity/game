package dev.jacobandersen.cams.game.security.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsSessionManager {
    private final ApplicationContext context;
    private final Map<String, SessionWrapper> sessions;

    @Autowired
    public WsSessionManager(ApplicationContext context) {
        this.context = context;
        sessions = new ConcurrentHashMap<>();
    }

    public void addSession(WebSocketSession session) {
        System.out.println("adding session id: " + session.getId());
        sessions.put(session.getId(), new SessionWrapper(
                session,
                SessionWrapper.SessionState.AUTHENTICATED,
                System.currentTimeMillis()
        ));
    }

    public void setSessionState(String sessionId, SessionWrapper.SessionState state) {
        final SessionWrapper wrapper = sessions.get(sessionId);
        if (wrapper != null) {
            sessions.put(sessionId, new SessionWrapper(wrapper.session, state, System.currentTimeMillis()));
        }
    }

    public void removeSession(String sessionId) {
        System.out.println("removing session id: " + sessionId);
        sessions.remove(sessionId);
    }

    @Scheduled(fixedRate = 1000)
    private void periodicReauthorization() {
        for (Map.Entry<String, SessionWrapper> entry : new HashSet<>(sessions.entrySet())) {
            final SessionWrapper sessionWrapper = entry.getValue();
            final WebSocketSession session = sessionWrapper.session;
            final Principal principal = session.getPrincipal();
            final Duration durationSinceLastStateUpdate = Duration.between(Instant.ofEpochMilli(sessionWrapper.stateTime), Instant.now());

            if (principal == null) {
                // should not happen
                System.err.printf("Websocket session with ID %s had a null principal during periodic reauthorization%n", session.getId());
                continue;
            }

            if (principal.getName().toLowerCase(Locale.ROOT).startsWith("guest")) {
                // guests do not need to reauthenticate
                continue;
            }

            switch (sessionWrapper.sessionState) {
                case AUTHENTICATED:
                    if (durationSinceLastStateUpdate.toSeconds() >= 30) {
                        context.getBean(SimpMessagingTemplate.class)
                                .convertAndSendToUser(principal.getName(), "/queue/reauthenticate", "Reauthentication required");

                        setSessionState(session.getId(), SessionWrapper.SessionState.AWAITING_REAUTHENTICATION);
                    }
                    break;
                case AWAITING_REAUTHENTICATION, FAILED_REAUTHENTICATION:
                    if (sessionWrapper.sessionState == SessionWrapper.SessionState.FAILED_REAUTHENTICATION || durationSinceLastStateUpdate.toSeconds() >= 30) {
                        try {
                            session.close(CloseStatus.POLICY_VIOLATION);
                        } catch (IOException ex) {
                            throw new RuntimeException("Failed closing delinquent websocket session", ex);
                        }
                    }
                    break;
            }
        }
    }

    public record SessionWrapper(WebSocketSession session, SessionState sessionState, Long stateTime) {
        public enum SessionState {
            AUTHENTICATED,
            AWAITING_REAUTHENTICATION,
            FAILED_REAUTHENTICATION;
        }
    }
}
