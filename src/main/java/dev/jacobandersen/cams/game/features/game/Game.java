package dev.jacobandersen.cams.game.features.game;

import dev.jacobandersen.cams.game.security.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.*;

@RedisHash("games")
public final class Game {
    @Id
    private UUID id;
    private UUID hostId;
    private Set<Player> players;
    private Set<Observer> observers;
    private GameState state;

    public Game() {
        players = new HashSet<>();
        observers = new HashSet<>();
        state = GameState.LOBBY;
    }

    public Game(User host) {
        this();
        id = UUID.randomUUID();
        hostId = host.id();
        players.add(new Player(host));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public boolean isPlayer(UUID userId) {
        return players.stream().anyMatch(u -> u.getId().equals(userId));
    }

    public Player addPlayer(final User user) {
        final Player player = new Player(user);
        players.add(player);
        return player;
    }

    public void removePlayer(final UUID userId) {
        players.removeIf(player -> player.getId().equals(userId));
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Observer> getObservers() {
        return observers;
    }

    public boolean isObserver(UUID userId) {
        return observers.stream().anyMatch(u -> u.getId().equals(userId));
    }

    public Observer addObserver(final User user) {
        final Observer observer = new Observer(user);
        observers.add(observer);
        return observer;
    }

    public void removeObserver(final UUID userId) {
        observers.removeIf(observer -> observer.getId().equals(userId));
    }

    public void setObservers(Set<Observer> observers) {
        this.observers = observers;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
