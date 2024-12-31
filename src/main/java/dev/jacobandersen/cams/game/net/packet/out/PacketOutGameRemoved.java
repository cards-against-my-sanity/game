package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutGameRemoved extends Packet {
    private UUID gameId;

    public PacketOutGameRemoved() {
        super(PacketType.GAME_REMOVED);
    }

    public PacketOutGameRemoved(UUID gameId) {
        this();
        this.gameId = gameId;
    }

    public UUID getGameId() {
        return gameId;
    }
}
