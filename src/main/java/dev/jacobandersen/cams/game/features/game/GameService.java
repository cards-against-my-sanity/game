package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.config.WsConfig;
import dev.jacobandersen.cams.game.error.game.GameNotFoundException;
import dev.jacobandersen.cams.game.error.game.UserAlreadyInGameException;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.out.*;
import dev.jacobandersen.cams.game.net.redis.RedisSystemMessagePublisher;
import dev.jacobandersen.cams.game.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    private final GameRepository repository;
    private final RedisSystemMessagePublisher publisher;

    @Autowired
    public GameService(GameRepository repository, RedisSystemMessagePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public Game findGameHostedBy(UUID hostId) {
        return repository.findByHostId(hostId);
    }

    public Game findGameWithPlayer(UUID userId) {
        for (Game game : repository.findAll()) {
            if (game.getPlayers().stream().anyMatch(player -> player.getId().equals(userId))) {
                return game;
            }
        }

        return null;
    }

    public Game findGameWithObserver(UUID userId) {
        for (Game game : repository.findAll()) {
            if (game.getObservers().stream().anyMatch(player -> player.getId().equals(userId))) {
                return game;
            }
        }

        return null;
    }

    public Game findGameWithPlayerOrObserver(UUID userId) {
        for (Game game : repository.findAll()) {
            if (game.getPlayers().stream().anyMatch(player -> player.getId().equals(userId)) || game.getObservers().stream().anyMatch(player -> player.getId().equals(userId))) {
                return game;
            }
        }

        return null;
    }

    public Game findGameForUser(UUID userId) {
        Game hosted = findGameHostedBy(userId);
        if (hosted != null) {
            return hosted;
        }

        return findGameWithPlayerOrObserver(userId);
    }

    public boolean isInGame(UUID userId) {
        return findGameHostedBy(userId) != null || findGameWithPlayerOrObserver(userId) != null;
    }

    public Game getGame(final UUID gameId) {
        return repository.findById(gameId).orElse(null);
    }

    public List<Game> listGames() {
        return repository.findAll();
    }

    public Game createGame(User user) throws UserAlreadyInGameException {
        if (isInGame(user.id())) {
            throw new UserAlreadyInGameException(user);
        }

        Game game = new Game(user);

        publisher.publishMessage(new PacketOutGameCreated(game), WsConfig.topic("gameBrowser"));

        repository.save(game);

        return game;
    }

    public void addUser(final UUID gameId, User user, boolean observer) throws UserAlreadyInGameException, GameNotFoundException {
        if (isInGame(user.id())) {
            throw new UserAlreadyInGameException(user);
        }

        Game game = getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }

        final Packet packet;
        if (observer) {
            packet = new PacketOutObserverJoinedGame(gameId, game.addObserver(user));
        } else {
            packet = new PacketOutPlayerJoinedGame(gameId, game.addPlayer(user));
        }

        repository.save(game);
        publisher.publishMessage(
                packet,
                WsConfig.topic("gameBrowser"), WsConfig.gameTopic(gameId)
        );
    }

    public void removeUserFromAssociatedGame(final UUID userId) {
        removeUser(findGameForUser(userId), userId);
    }

    public void removeUser(Game game, final UUID userId) {
        if (game == null) return;

        final UUID gameId = game.getId();

        if (game.getHostId().equals(userId)) {
            game.setState(GameState.ABANDONED);

            publisher.publishMessage(
                    new PacketOutGameStateChange(gameId, GameState.ABANDONED),
                    WsConfig.topic("gameBrowser"), WsConfig.gameTopic(gameId)
            );
        }

        if (game.isPlayer(userId)) {
            game.removePlayer(userId);

            publisher.publishMessage(
                    new PacketOutPlayerLeftGame(gameId, userId),
                    WsConfig.topic("gameBrowser"), WsConfig.gameTopic(gameId)
            );
        } else if (game.isObserver(userId)) {
            game.removeObserver(userId);

            publisher.publishMessage(
                    new PacketOutObserverLeftGame(gameId, userId),
                    WsConfig.topic("gameBrowser"), WsConfig.gameTopic(gameId)
            );
        }

        game = repository.save(game);

        if (game.getPlayers().isEmpty() && game.getObservers().isEmpty()) {
            publisher.publishMessage(
                    new PacketOutGameRemoved(gameId),
                    WsConfig.topic("gameBrowser")
            );

            repository.delete(game);
        }
    }
}
