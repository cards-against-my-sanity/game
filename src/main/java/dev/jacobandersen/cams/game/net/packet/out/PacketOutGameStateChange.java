package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.features.game.GameState;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutGameStateChange extends Packet {
    private UUID gameId;
    private GameState state;

    public PacketOutGameStateChange() {
        super(PacketType.STATE_CHANGE);
    }

    public PacketOutGameStateChange(UUID gameId, GameState state) {
        this();
        this.gameId = gameId;
        this.state = state;
    }

    public UUID getGameId() {
        return gameId;
    }

    public GameState getState() {
        return state;
    }
}
