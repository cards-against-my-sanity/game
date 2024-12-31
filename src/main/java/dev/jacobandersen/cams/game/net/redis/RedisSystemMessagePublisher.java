package dev.jacobandersen.cams.game.net.redis;

import dev.jacobandersen.cams.game.config.RedisConfig;
import dev.jacobandersen.cams.game.net.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSystemMessagePublisher {
    private final RedisTemplate<String, Object> template;

    @Autowired
    public RedisSystemMessagePublisher(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public final void publishMessage(Packet packet, String channel) {
        publishMessage(packet, new String[] { channel });
    }

    public final void publishMessage(Packet packet, String... channels) {
        for (String channel : channels) {
            template.convertAndSend(RedisConfig.CHANNEL, new RedisSystemMessage(channel, packet));
            // TODO: logging
        }
    }
}
