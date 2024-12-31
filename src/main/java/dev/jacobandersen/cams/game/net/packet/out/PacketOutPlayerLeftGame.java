package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutPlayerLeftGame extends Packet {
    private UUID gameId;
    private UUID playerId;

    public PacketOutPlayerLeftGame() {
        super(PacketType.PLAYER_LEFT_GAME);
    }

    public PacketOutPlayerLeftGame(UUID gameId, UUID playerId) {
        this();
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
