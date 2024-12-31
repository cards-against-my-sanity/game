package dev.jacobandersen.cams.game.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.jacobandersen.cams.game.net.packet.Packet;
import dev.jacobandersen.cams.game.net.packet.PacketRegistry;
import dev.jacobandersen.cams.game.net.packet.PacketDeserializer;
import dev.jacobandersen.cams.game.net.packet.PacketSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper(PacketRegistry registry) {
        final ObjectMapper mapper = new ObjectMapper();

        // serialize by fields, not getters
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);

        // serialize even if null
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // packet handling
        SimpleModule module = new SimpleModule();
        module.addSerializer(Packet.class, new PacketSerializer());
        module.addDeserializer(Packet.class, new PacketDeserializer(registry));

        mapper.registerModule(module);

        return mapper;
    }
}
