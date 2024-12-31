package dev.jacobandersen.cams.game.net.redis;

import dev.jacobandersen.cams.game.net.packet.Packet;

public record RedisSystemMessage(String wsTopic, Packet packet) {
}
