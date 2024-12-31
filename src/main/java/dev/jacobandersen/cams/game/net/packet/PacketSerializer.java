package dev.jacobandersen.cams.game.net.packet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;

public class PacketSerializer extends JsonSerializer<Packet> {
    @Override
    public void serialize(Packet packet, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("type", packet.getType().name());

        gen.writeFieldName("payload");
        gen.writeStartObject();
        for (final Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                final Object value = field.get(packet);
                gen.writeObjectField(field.getName(), value);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(String.format("Could not serialize packet of type %s", packet.getType()), ex);
            }
        }
        gen.writeEndObject();

        gen.writeEndObject();
    }
}
