package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.features.game.Observer;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutObserverJoinedGame extends Packet {
    private UUID gameId;
    private Observer observer;

    public PacketOutObserverJoinedGame() {
        super(PacketType.OBSERVER_JOINED_GAME);
    }

    public PacketOutObserverJoinedGame(UUID gameId, Observer observer) {
        this();
        this.gameId = gameId;
        this.observer = observer;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Observer getObserver() {
        return observer;
    }
}
