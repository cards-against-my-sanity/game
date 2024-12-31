package dev.jacobandersen.cams.game.net.packet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PacketDeserializer extends JsonDeserializer<Packet> {
    private final PacketRegistry registry;

    public PacketDeserializer(PacketRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Packet deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        final String rawType = node.get("type").asText();

        final PacketType type = PacketType.getByName(rawType);
        if (type == null) {
            throw new IllegalArgumentException("Unknown packet type: " + rawType);
        }

        final ObjectNode tmp = (ObjectNode) p.getCodec().createObjectNode();
        tmp.set("type", node.get("type"));
        node.get("payload").fields().forEachRemaining(field -> tmp.set(field.getKey(), field.getValue()));

        return p.getCodec().treeToValue(tmp, registry.getPacketType(type));
    }
}
