package dev.jacobandersen.cams.game.features.chat;

import dev.jacobandersen.cams.game.config.WsConfig;
import dev.jacobandersen.cams.game.features.game.Game;
import dev.jacobandersen.cams.game.features.game.GameService;
import dev.jacobandersen.cams.game.net.packet.out.PacketOutChat;
import dev.jacobandersen.cams.game.net.redis.RedisSystemMessagePublisher;
import dev.jacobandersen.cams.game.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatService {
    private final RedisSystemMessagePublisher publisher;
    private final GameService gameService;

    @Autowired
    public ChatService(RedisSystemMessagePublisher publisher, GameService gameService) {
        this.publisher = publisher;
        this.gameService = gameService;
    }

    public void sendGlobalChatMessage(String message, User from) {
        publisher.publishMessage(new PacketOutChat(message, from), WsConfig.topic("gameBrowser"));
    }

    public void sendLocalChatMessage(String message, User from, UUID gameId) {
        final Game game = gameService.getGame(gameId);
        if (game == null) return;

        final UUID userId = from.id();
        if (game.isPlayer(userId) || game.isObserver(userId)) {
            publisher.publishMessage(new PacketOutChat(message, from), WsConfig.gameTopic(gameId));
        }
    }
}
