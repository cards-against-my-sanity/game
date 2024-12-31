package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.features.game.Game;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

public class PacketOutGameCreated extends Packet {
    private Game game;

    public PacketOutGameCreated() {
        super(PacketType.GAME_CREATED);
    }

    public PacketOutGameCreated(Game game) {
        this();
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
