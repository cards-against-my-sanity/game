package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.features.game.Player;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutPlayerJoinedGame extends Packet {
    private UUID gameId;
    private Player player;

    public PacketOutPlayerJoinedGame() {
        super(PacketType.PLAYER_JOINED_GAME);
    }

    public PacketOutPlayerJoinedGame(UUID gameId, Player player) {
        this();
        this.gameId = gameId;
        this.player = player;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Player getPlayer() {
        return player;
    }
}
