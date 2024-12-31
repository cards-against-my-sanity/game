package dev.jacobandersen.cams.game.net.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisSystemMessageSubscriber implements MessageListener {
    private final SimpMessagingTemplate wsTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisSystemMessageSubscriber(SimpMessagingTemplate wsTemplate, ObjectMapper objectMapper) {
        this.wsTemplate = wsTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Receives a system message and rebroadcasts it to the local websocket.
     *
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        RedisSystemMessage systemMessage;
        try {
            systemMessage = objectMapper.readValue(message.getBody(), RedisSystemMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        wsTemplate.convertAndSend(systemMessage.wsTopic(), systemMessage.packet());
    }
}
