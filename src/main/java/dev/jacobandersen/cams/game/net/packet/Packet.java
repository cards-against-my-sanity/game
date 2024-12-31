package dev.jacobandersen.cams.game.net.packet;

public abstract class Packet {
    private final PacketType type;

    public Packet(final PacketType type) {
        this.type = type;
    }

    public final PacketType getType() {
        return type;
    }
}
