package dev.jacobandersen.cams.game.features.chat;

import dev.jacobandersen.cams.game.security.User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/globalChat")
    public void handleChatMessage(@Payload String message, User sender) {
        chatService.sendGlobalChatMessage(message, sender);
    }

    @MessageMapping("/game/{gameId}/chat")
    public void handleLocalChatMessage(@DestinationVariable UUID gameId, @Payload String message, User sender) {
        chatService.sendLocalChatMessage(message, sender, gameId);
    }
}
