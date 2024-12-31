package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutObserverLeftGame extends Packet {
    private UUID gameId;
    private UUID observerId;

    public PacketOutObserverLeftGame() {
        super(PacketType.PLAYER_LEFT_GAME);
    }

    public PacketOutObserverLeftGame(UUID gameId, UUID observerId) {
        this();
        this.gameId = gameId;
        this.observerId = observerId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getObserverId() {
        return observerId;
    }
}
