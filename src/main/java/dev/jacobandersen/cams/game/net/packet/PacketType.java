package dev.jacobandersen.cams.game.net.packet;

import dev.jacobandersen.cams.game.net.packet.out.*;

import java.util.Arrays;

public enum PacketType {
    CHAT(PacketOutChat.class),
    STATE_CHANGE(PacketOutGameStateChange.class),
    GAME_CREATED(PacketOutGameCreated.class),
    GAME_REMOVED(PacketOutGameRemoved.class),
    GAME_SETTINGS_UPDATED(PacketOutGameSettingsUpdated.class),
    PLAYER_JOINED_GAME(PacketOutPlayerJoinedGame.class),
    PLAYER_LEFT_GAME(PacketOutPlayerLeftGame.class),
    OBSERVER_JOINED_GAME(PacketOutObserverJoinedGame.class),
    OBSERVER_LEFT_GAME(PacketOutObserverLeftGame.class);

    private final Class<? extends Packet> type;

    PacketType(Class<? extends Packet> type) {
        this.type = type;
    }

    public Class<? extends Packet> getType() {
        return type;
    }

    public static PacketType getByClass(Class<? extends Packet> clazz) {
        return Arrays.stream(values())
                .filter(packetType -> packetType.getType().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    public static PacketType getByName(final String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
