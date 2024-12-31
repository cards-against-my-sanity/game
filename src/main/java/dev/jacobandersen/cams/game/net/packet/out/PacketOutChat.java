package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;
import dev.jacobandersen.cams.game.security.User;

public class PacketOutChat extends Packet {
    private String message;
    private User sender;

    public PacketOutChat() {
        super(PacketType.CHAT);
    }

    public PacketOutChat(String message, User sender) {
        this();
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }
}
