package dev.jacobandersen.cams.game.net.packet;

import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PacketRegistry {
    private final Map<PacketType, Class<? extends Packet>> packetTypes;

    public PacketRegistry() {
        packetTypes = new HashMap<>();
    }

    public void registerPacket(PacketType type, Class<? extends Packet> clazz) {
        packetTypes.put(type, clazz);
    }

    public Class<? extends Packet> getPacketType(PacketType type) {
        return packetTypes.get(type);
    }

    @PostConstruct
    public final void registerPackets() {
        final Reflections reflections = new Reflections("dev.jacobandersen.cams.game.net.packet");

        for (final Class<? extends Packet> packetClazz : reflections.getSubTypesOf(Packet.class)) {
            final PacketType type = PacketType.getByClass(packetClazz);
            if (null == type) {
                System.err.printf("Skipping packet %s because it does not have an associated PacketType...\n", packetClazz.getSimpleName());
                continue;
            }

            registerPacket(type, packetClazz);
        }
    }
}
