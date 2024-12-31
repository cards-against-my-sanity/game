package dev.jacobandersen.cams.game.net.packet.out;

import dev.jacobandersen.cams.game.features.game.GameSettings;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketType;

import java.util.UUID;

public class PacketOutGameSettingsUpdated extends Packet {
    private UUID gameId;
    private GameSettings settings;

    public PacketOutGameSettingsUpdated() {
        super(PacketType.GAME_SETTINGS_UPDATED);
    }

    public PacketOutGameSettingsUpdated(UUID gameId, GameSettings settings) {
        this();
        this.gameId = gameId;
        this.settings = settings;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }
}
