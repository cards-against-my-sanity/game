package dev.jacobandersen.cams.game.config;

import dev.jacobandersen.cams.game.features.game.GameService;
import dev.jacobandersen.cams.game.security.websocket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocketSecurity
public class WsConfig implements WebSocketMessageBrokerConfigurer {
    private static final String TOPIC_PREFIX = "/topic";
    private static final String QUEUE_PREFIX = "/queue";
    private final GameService gameService;

    public static String topic(String topic) {
        return TOPIC_PREFIX + "/" + topic;
    }

    public static String gameTopic(UUID gameId) {
        return topic(String.format("game/%s", gameId));
    }

    public static String queue(String queue) {
        return QUEUE_PREFIX + "/" + queue;
    }

    private final WsHandshakeInterceptor wsHandshakeInterceptor;
    private final CustomStompErrorHandler customStompErrorHandler;
    private final WsSessionManager wsSessionManager;

    @Autowired
    public WsConfig(WsHandshakeInterceptor wsHandshakeInterceptor, CustomStompErrorHandler customStompErrorHandler, WsSessionManager wsSessionManager, GameService gameService) {
        this.wsHandshakeInterceptor = wsHandshakeInterceptor;
        this.customStompErrorHandler = customStompErrorHandler;
        this.wsSessionManager = wsSessionManager;
        this.gameService = gameService;
    }

    @Value("${application.frontend-origin}")
    private String frontendOrigin;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        taskScheduler.setThreadNamePrefix("stomp-heartbeat-thread-");
        taskScheduler.initialize();

        config.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker(TOPIC_PREFIX, QUEUE_PREFIX);
//                .setHeartbeatValue(new long[]{5000, 5000})
//                .setTaskScheduler(taskScheduler);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(handler -> new WsConnectionDecorator(handler, wsSessionManager, gameService));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs")
                .setAllowedOrigins(frontendOrigin)
                .addInterceptors(wsHandshakeInterceptor)
                .setHandshakeHandler(new WsHandshakeHandler());

        registry.setErrorHandler(customStompErrorHandler);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new WsUserArgumentResolver());
    }

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.DISCONNECT).permitAll()
                .simpDestMatchers("/app/game/list").permitAll()
                .simpDestMatchers("/app/admin/**").hasRole("admin")
                .simpSubscribeDestMatchers("/topic/gameBrowser", "/user/queue/**").permitAll()
                .anyMessage().hasAnyRole("user", "moderator", "admin")
                .build();
    }
}
