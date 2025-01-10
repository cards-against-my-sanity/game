package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.config.WsConfig;
import dev.jacobandersen.cams.game.dto.ChangeGameSettingsDto;
import dev.jacobandersen.cams.game.dto.UpdateGameDecksDto;
import dev.jacobandersen.cams.game.error.game.GameNotFoundException;
import dev.jacobandersen.cams.game.error.game.UserAlreadyInGameException;
import dev.jacobandersen.cams.game.features.deck.DeckService;
import dev.jacobandersen.cams.game.features.deck.DeckWithCards;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.out.*;
import dev.jacobandersen.cams.game.net.redis.RedisSystemMessagePublisher;
import dev.jacobandersen.cams.game.security.User;
import org.bouncycastle.util.Pack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    private final GameRepository repository;
    private final RedisSystemMessagePublisher publisher;
    private final DeckService deckService;

    @Autowired
    public GameService(GameRepository repository, RedisSystemMessagePublisher publisher, DeckService deckService) {
        this.repository = repository;
        this.publisher = publisher;
        this.deckService = deckService;
    }

    public boolean isHostOf(UUID userId, UUID gameId) {
        final Game game = getGame(gameId);
        if (game == null) {
            return false;
        }

        return game.getHostId().equals(userId);
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

    public void updateGameSettings(final UUID gameId, final ChangeGameSettingsDto dto) {
        final Game game = getGame(gameId);
        if (game == null) return;

        final GameSettings settings = game.getSettings();

        dto.maxPlayers().ifPresent(maxPlayers -> {
            if (maxPlayers < GameConstants.MINIMUM_PLAYERS) {
                maxPlayers = GameConstants.MINIMUM_PLAYERS;
            }

            settings.setMaxPlayers(maxPlayers);
        });

        dto.maxObservers().ifPresent(maxObservers -> {
            if (maxObservers < 0) {
                maxObservers = 0;
            }

            settings.setMaxObservers(maxObservers);
        });

        dto.maxScore().ifPresent(maxScore -> {
            if (maxScore < 1) {
                maxScore = 1;
            }

            settings.setMaxScore(maxScore);
        });

        dto.roundIntermissionTimer().ifPresent(roundIntermissionTimer -> {
            if (roundIntermissionTimer < 0) {
                roundIntermissionTimer = 0;
            } else if (roundIntermissionTimer > GameConstants.MAX_TIMER_VALUE) {
                roundIntermissionTimer = GameConstants.MAX_TIMER_VALUE;
            }

            settings.setRoundIntermissionTimer(roundIntermissionTimer);
        });

        dto.gameWinIntermissionTimer().ifPresent(gameWinIntermissionTimer -> {
            if (gameWinIntermissionTimer < 0) {
                gameWinIntermissionTimer = 0;
            } else if (gameWinIntermissionTimer > GameConstants.MAX_TIMER_VALUE) {
                gameWinIntermissionTimer = GameConstants.MAX_TIMER_VALUE;
            }

            settings.setGameWinIntermissionTimer(gameWinIntermissionTimer);
        });

        dto.playingTimer().ifPresent(playingTimer -> {
            if (playingTimer < 0) {
                playingTimer = 0;
            } else if (playingTimer > GameConstants.MAX_TIMER_VALUE) {
                playingTimer = GameConstants.MAX_TIMER_VALUE;
            }

            settings.setPlayingTimer(playingTimer);
        });

        dto.judgingTimer().ifPresent(judgingTimer -> {
            if (judgingTimer < 0) {
                judgingTimer = 0;
            } else if (judgingTimer > GameConstants.MAX_TIMER_VALUE) {
                judgingTimer = GameConstants.MAX_TIMER_VALUE;
            }

            settings.setJudgingTimer(judgingTimer);
        });

        dto.allowPlayersToJoinMidGame().ifPresent(settings::setAllowPlayersToJoinMidGame);

        game.setSettings(settings);
        repository.save(game);

        publisher.publishMessage(
                new PacketOutGameSettingsUpdated(gameId, settings),
                WsConfig.topic("gameBrowser"), WsConfig.gameTopic(gameId)
        );
    }

    public void updateGameDecks(final UUID gameId, final UpdateGameDecksDto dto) {
        final Game game = getGame(gameId);
        if (game == null) return;

        List<DeckWithCards> loaded = deckService.getDecksWithCards(dto.deckIds());
        game.setDecks(new HashSet<>(loaded));
        repository.save(game);

        publisher.publishMessage(
                new PacketOutGameDecksUpdated(gameId, loaded),
                WsConfig.topic("gameBrowser"), WsConfig.gameTopic(gameId)
        );
    }
}
